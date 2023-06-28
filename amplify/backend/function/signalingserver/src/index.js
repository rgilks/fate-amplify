/* Amplify Params - DO NOT EDIT
	API_FATE_GRAPHQLAPIIDOUTPUT
	API_FATE_YWEBRTCTOPICTABLE_ARN
	API_FATE_YWEBRTCTOPICTABLE_NAME
	ENV
	REGION
Amplify Params - DO NOT EDIT */ /* Amplify Params - DO NOT EDIT
	
Amplify Params - DO NOT EDIT */

const {Configuration, OpenAIApi} = require('openai')
const AWS = require('aws-sdk')
const dynamoDb = new AWS.DynamoDB()

const {REGION, API_FATE_YWEBRTCTOPICTABLE_NAME} = process.env

const config = new Configuration({apiKey: process.env.OPENAI_API_KEY})
const openai = new OpenAIApi(config)

const scanItems = async ({params, lastKey = undefined, items = []}) => {
  if (lastKey) {
    params.ExclusiveStartKey = lastKey
  }

  const {Items, LastEvaluatedKey} = await dynamoDb.scan(params).promise()

  const result = Items.length ? items.concat(Items) : items

  return LastEvaluatedKey
    ? await scanItems({
        params,
        lastKey: LastEvaluatedKey,
        items: result
      })
    : result
}

// TODO: go via appsync?
const subscribe = async (topic, connectionId) => {
  const now = new Date()
  try {
    return await dynamoDb
      .updateItem({
        TableName: API_FATE_YWEBRTCTOPICTABLE_NAME,
        Key: {name: {S: topic}},
        UpdateExpression:
          'ADD receivers :r ' +
          'SET updatedAt = :updatedAt, ' +
          'createdAt = if_not_exists(createdAt, :createdAt), ' +
          '#lastChangedAt = :lastChangedAt, ' +
          '#typename = :typename, ' +
          '#version = if_not_exists(#version, :initial) + :num',
        ExpressionAttributeNames: {
          '#lastChangedAt': '_lastChangedAt',
          '#typename': '__typename',
          '#version': '_version'
        },
        ExpressionAttributeValues: {
          ':r': {SS: [connectionId]},
          ':updatedAt': {S: now.toISOString()},
          ':createdAt': {S: now.toISOString()},
          ':lastChangedAt': {N: now.getTime().toString()},
          ':typename': {S: 'YWebRtcTopic'},
          ':num': {N: '1'},
          ':initial': {N: '0'}
        }
      })
      .promise()
  } catch (err) {
    console.log(`Cannot update topic ${topic}: ${err.message}`)
  }
}

// TODO: go via appsync?
const unsubscribe = async (topic, connectionId) => {
  const now = new Date()
  try {
    return await dynamoDb
      .updateItem({
        TableName: API_FATE_YWEBRTCTOPICTABLE_NAME,
        Key: {name: {S: topic}},
        UpdateExpression:
          'DELETE receivers :r ' +
          'SET updatedAt = :updatedAt, ' +
          'createdAt = if_not_exists(createdAt, :createdAt), ' +
          '#lastChangedAt = :lastChangedAt, ' +
          '#typename = :typename, ' +
          '#version = if_not_exists(#version, :initial) + :num',
        ExpressionAttributeNames: {
          '#lastChangedAt': '_lastChangedAt',
          '#typename': '__typename',
          '#version': '_version'
        },
        ExpressionAttributeValues: {
          ':r': {SS: [connectionId]},
          ':updatedAt': {S: now.toISOString()},
          ':createdAt': {S: now.toISOString()},
          ':lastChangedAt': {N: now.getTime().toString()},
          ':typename': {S: 'YWebRtcTopic'},
          ':num': {N: '1'},
          ':initial': {N: '0'}
        }
      })
      .promise()
  } catch (err) {
    console.log(`Cannot update topic ${topic}: ${err.message}`)
  }
}

const getReceivers = async topic => {
  try {
    const {Item: item} = await dynamoDb
      .getItem({
        TableName: API_FATE_YWEBRTCTOPICTABLE_NAME,
        Key: {name: {S: topic}}
      })
      .promise()
    return item?.receivers ? item.receivers.SS : []
  } catch (err) {
    console.log(`Cannot get topic ${topic}: ${err.message}`)
    return []
  }
}

const handleYWebRtcMessage = async (connectionId, message, send) => {
  const promises = []

  if (message && message.type) {
    switch (message.type) {
      case 'subscribe':
        ;(message.topics || []).forEach(topic => {
          promises.push(subscribe(topic, connectionId))
        })
        break
      case 'unsubscribe':
        ;(message.topics || []).forEach(topic => {
          promises.push(unsubscribe(topic, connectionId))
        })
        break
      case 'publish':
        if (message.topic) {
          const receivers = await getReceivers(message.topic)
          receivers.forEach(receiver => {
            promises.push(send(receiver, message))
          })
        }
        break
      case 'ping':
        promises.push(send(connectionId, {type: 'pong'}))
        break
    }
  }

  await Promise.all(promises)
}

const handleOpenAIMessage = async (messages, send) => {
  console.log('messages:' + messages)

  const response = await openai.createChatCompletion(
    {
      model: 'gpt-4',
      messages: [
        {role: 'system', content: 'You are a helpful assistant.'},
        {role: 'user', content: 'Who won the world series in 2020?'},
        {role: 'assistant', content: 'The Los Angeles Dodgers.'},
        {role: 'user', content: 'Where was it played?'}
      ],
      max_tokens: 50,
      n: 1,
      stop: null,
      temperature: 1,
      stream: true
    },
    {responseType: 'stream'}
  )

  // console.log('response:' + response.data)

  const receivers = await getReceivers('example-document')
  console.log('receivers:' + receivers)

  return new Promise(resolve => {
    let result = ''
    response.data.on('data', data => {
      const lines = data
        ?.toString()
        ?.split('\n')
        .filter(line => line.trim() !== '')
      for (const line of lines) {
        const message = line.replace(/^data: /, '')
        if (message == '[DONE]') {
          console.log('DONE')
          resolve(result)
        } else {
          let token
          try {
            token = JSON.parse(message)?.choices?.[0]?.delta?.content
          } catch {
            console.log('ERROR', json)
          }
          result += token
          if (token) {
            console.log('token:' + token)
            receivers.forEach(receiver => {
              send(receiver, {type: 'token', value: token})
            })
          }
        }
      }
    })
  })
}

const handleConnect = connectionId => {
  console.log(`Connected: ${connectionId}`)
}

const handleDisconnect = async connectionId => {
  console.log(`Disconnected: ${connectionId}`)

  const items = await scanItems({
    params: {
      TableName: API_FATE_YWEBRTCTOPICTABLE_NAME
    }
  })

  const promises = items.map(item => {
    const receivers = item.receivers?.SS ?? []
    if (receivers.includes(connectionId)) {
      return unsubscribe(item.name.S, connectionId)
    }
  })

  await Promise.all(promises)
}

exports.handler = async event => {
  if (event.body !== '{"type":"ping"}') {
    console.log(event)
  }

  if (!API_FATE_YWEBRTCTOPICTABLE_NAME) {
    return {statusCode: 502, body: 'Not configured'}
  }

  const apigwManagementApi = new AWS.ApiGatewayManagementApi({
    apiVersion: '2018-11-29',
    endpoint: `https://${event.requestContext.apiId}.execute-api.${REGION}.amazonaws.com/${event.requestContext.stage}`
  })

  const send = async (connectionId, message) => {
    try {
      await apigwManagementApi
        .postToConnection({
          ConnectionId: connectionId,
          Data: JSON.stringify(message)
        })
        .promise()
    } catch (err) {
      if (err.statusCode === 410) {
        console.log(`Found stale connection, deleting ${connectionId}`)
        await handleDisconnect(connectionId)
      } else {
        console.log(`Error when sending to ${connectionId}: ${err.message}`)
      }
    }
  }

  try {
    switch (event.requestContext.routeKey) {
      case '$connect':
        handleConnect(event.requestContext.connectionId)
        break
      case '$disconnect':
        await handleDisconnect(event.requestContext.connectionId)
        break
      case 'openaimessages':
        await handleOpenAIMessage(JSON.parse(event.body).messages, send)
        break
      case '$default':
        await handleYWebRtcMessage(
          event.requestContext.connectionId,
          JSON.parse(event.body),
          send
        )
        break
    }

    return {statusCode: 200}
  } catch (err) {
    console.log(`Error ${event.requestContext.connectionId}`, err)
    return {statusCode: 500, body: err.message}
  }
}
