// @ts-check
import {initSchema} from '@aws-amplify/datastore'
import {schema} from './schema'

const {
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
