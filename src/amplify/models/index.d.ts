import {
  ModelInit,
  MutableModel,
  __modelMeta__,
  ManagedIdentifier
} from '@aws-amplify/datastore'
// @ts-ignore
import {
  LazyLoading,
  LazyLoadingDisabled,
  AsyncCollection,
  AsyncItem
} from '@aws-amplify/datastore'

type EagerAspect = {
  readonly type: string
  readonly phrase: string
}

type LazyAspect = {
  readonly type: string
  readonly phrase: string
}

export declare type Aspect = LazyLoading extends LazyLoadingDisabled
  ? EagerAspect
  : LazyAspect

export declare const Aspect: new (init: ModelInit<Aspect>) => Aspect

type EagerSkill = {
  readonly name: string
  readonly rating: string
}

type LazySkill = {
  readonly name: string
  readonly rating: string
}

export declare type Skill = LazyLoading extends LazyLoadingDisabled
  ? EagerSkill
  : LazySkill

export declare const Skill: new (init: ModelInit<Skill>) => Skill

type EagerStunt = {
  readonly name: string
  readonly description: string
}

type LazyStunt = {
  readonly name: string
  readonly description: string
}

export declare type Stunt = LazyLoading extends LazyLoadingDisabled
  ? EagerStunt
  : LazyStunt

export declare const Stunt: new (init: ModelInit<Stunt>) => Stunt

type EagerStress = {
  readonly total: number
  readonly current: number
}

type LazyStress = {
  readonly total: number
  readonly current: number
}

export declare type Stress = LazyLoading extends LazyLoadingDisabled
  ? EagerStress
  : LazyStress

export declare const Stress: new (init: ModelInit<Stress>) => Stress

type EagerStressMap = {
  readonly physical: Stress
  readonly mental: Stress
}

type LazyStressMap = {
  readonly physical: Stress
  readonly mental: Stress
}

export declare type StressMap = LazyLoading extends LazyLoadingDisabled
  ? EagerStressMap
  : LazyStressMap

export declare const StressMap: new (init: ModelInit<StressMap>) => StressMap

type EagerFatePoints = {
  readonly refresh: number
  readonly current: number
}

type LazyFatePoints = {
  readonly refresh: number
  readonly current: number
}

export declare type FatePoints = LazyLoading extends LazyLoadingDisabled
  ? EagerFatePoints
  : LazyFatePoints

export declare const FatePoints: new (init: ModelInit<FatePoints>) => FatePoints

type EagerPlayer = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Player, 'id'>
  }
  readonly id: string
  readonly gameID: string
  readonly characters?: (Character | null)[] | null
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

type LazyPlayer = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Player, 'id'>
  }
  readonly id: string
  readonly gameID: string
  readonly characters: AsyncCollection<Character>
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

export declare type Player = LazyLoading extends LazyLoadingDisabled
  ? EagerPlayer
  : LazyPlayer

export declare const Player: (new (init: ModelInit<Player>) => Player) & {
  copyOf(
    source: Player,
    mutator: (draft: MutableModel<Player>) => MutableModel<Player> | void
  ): Player
}

type EagerGame = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Game, 'id'>
  }
  readonly id: string
  readonly name?: string | null
  readonly slug?: string | null
  readonly players?: (Player | null)[] | null
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

type LazyGame = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Game, 'id'>
  }
  readonly id: string
  readonly name?: string | null
  readonly slug?: string | null
  readonly players: AsyncCollection<Player>
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

export declare type Game = LazyLoading extends LazyLoadingDisabled
  ? EagerGame
  : LazyGame

export declare const Game: (new (init: ModelInit<Game>) => Game) & {
  copyOf(
    source: Game,
    mutator: (draft: MutableModel<Game>) => MutableModel<Game> | void
  ): Game
}

type EagerCharacter = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Character, 'id'>
  }
  readonly id: string
  readonly name: string
  readonly aspects: (Aspect | null)[]
  readonly skills: (Skill | null)[]
  readonly stunts: (Stunt | null)[]
  readonly stress: StressMap
  readonly fate: FatePoints
  readonly background?: string | null
  readonly notes?: string | null
  readonly playerID?: string | null
  readonly player?: Player | null
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

type LazyCharacter = {
  readonly [__modelMeta__]: {
    identifier: ManagedIdentifier<Character, 'id'>
  }
  readonly id: string
  readonly name: string
  readonly aspects: (Aspect | null)[]
  readonly skills: (Skill | null)[]
  readonly stunts: (Stunt | null)[]
  readonly stress: StressMap
  readonly fate: FatePoints
  readonly background?: string | null
  readonly notes?: string | null
  readonly playerID?: string | null
  readonly player: AsyncItem<Player | undefined>
  readonly createdAt?: string | null
  readonly updatedAt?: string | null
  readonly owner?: string | null
}

export declare type Character = LazyLoading extends LazyLoadingDisabled
  ? EagerCharacter
  : LazyCharacter

export declare const Character: (new (
  init: ModelInit<Character>
) => Character) & {
  copyOf(
    source: Character,
    mutator: (draft: MutableModel<Character>) => MutableModel<Character> | void
  ): Character
}
