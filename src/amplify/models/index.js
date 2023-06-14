// @ts-check
import {initSchema} from '@aws-amplify/datastore'
import {schema} from './schema'

const {
  YWebRtcTopic,
  Player,
  Game,
  Character,
  Aspect,
  Skill,
  Stunt,
  Stress,
  StressMap,
  FatePoints
} = initSchema(schema)

export {
  YWebRtcTopic,
  Player,
  Game,
  Character,
  Aspect,
  Skill,
  Stunt,
  Stress,
  StressMap,
  FatePoints
}
