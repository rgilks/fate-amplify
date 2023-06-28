export const schema = {
  models: {
    YWebRtcTopic: {
      name: 'YWebRtcTopic',
      fields: {
        name: {
          name: 'name',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        },
        receivers: {
          name: 'receivers',
          isArray: true,
          type: 'String',
          isRequired: true,
          attributes: [],
          isArrayNullable: false
        },
        createdAt: {
          name: 'createdAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        },
        updatedAt: {
          name: 'updatedAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        }
      },
      syncable: true,
      pluralName: 'YWebRtcTopics',
      attributes: [
        {
          type: 'model',
          properties: {}
        },
        {
          type: 'key',
          properties: {
            fields: ['name']
          }
        },
        {
          type: 'auth',
          properties: {
            rules: [
              {
                allow: 'private',
                operations: ['create', 'update', 'delete', 'read']
              },
              {
                allow: 'private',
                provider: 'iam',
                operations: ['create', 'update', 'delete', 'read']
              }
            ]
          }
        }
      ]
    },
    Player: {
      name: 'Player',
      fields: {
        id: {
          name: 'id',
          isArray: false,
          type: 'ID',
          isRequired: true,
          attributes: []
        },
        gameID: {
          name: 'gameID',
          isArray: false,
          type: 'ID',
          isRequired: true,
          attributes: []
        },
        characters: {
          name: 'characters',
          isArray: true,
          type: {
            model: 'Character'
          },
          isRequired: false,
          attributes: [],
          isArrayNullable: true,
          association: {
            connectionType: 'HAS_MANY',
            associatedWith: ['player']
          }
        },
        createdAt: {
          name: 'createdAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        },
        updatedAt: {
          name: 'updatedAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        },
        owner: {
          name: 'owner',
          isArray: false,
          type: 'String',
          isRequired: false,
          attributes: []
        }
      },
      syncable: true,
      pluralName: 'Players',
      attributes: [
        {
          type: 'model',
          properties: {}
        },
        {
          type: 'key',
          properties: {
            name: 'byGame',
            fields: ['gameID']
          }
        },
        {
          type: 'auth',
          properties: {
            rules: [
              {
                allow: 'private',
                operations: ['create', 'update', 'delete', 'read']
              },
              {
                provider: 'userPools',
                ownerField: 'owner',
                allow: 'owner',
                identityClaim: 'cognito:username',
                operations: ['create', 'update', 'delete', 'read']
              }
            ]
          }
        }
      ]
    },
    Game: {
      name: 'Game',
      fields: {
        id: {
          name: 'id',
          isArray: false,
          type: 'ID',
          isRequired: true,
          attributes: []
        },
        name: {
          name: 'name',
          isArray: false,
          type: 'String',
          isRequired: false,
          attributes: []
        },
        slug: {
          name: 'slug',
          isArray: false,
          type: 'String',
          isRequired: false,
          attributes: []
        },
        players: {
          name: 'players',
          isArray: true,
          type: {
            model: 'Player'
          },
          isRequired: false,
          attributes: [],
          isArrayNullable: true,
          association: {
            connectionType: 'HAS_MANY',
            associatedWith: ['gameID']
          }
        },
        createdAt: {
          name: 'createdAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        },
        updatedAt: {
          name: 'updatedAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        },
        owner: {
          name: 'owner',
          isArray: false,
          type: 'String',
          isRequired: false,
          attributes: []
        }
      },
      syncable: true,
      pluralName: 'Games',
      attributes: [
        {
          type: 'model',
          properties: {}
        },
        {
          type: 'auth',
          properties: {
            rules: [
              {
                allow: 'private',
                operations: ['create', 'update', 'delete', 'read']
              },
              {
                provider: 'userPools',
                ownerField: 'owner',
                allow: 'owner',
                identityClaim: 'cognito:username',
                operations: ['create', 'update', 'delete', 'read']
              }
            ]
          }
        }
      ]
    },
    Character: {
      name: 'Character',
      fields: {
        id: {
          name: 'id',
          isArray: false,
          type: 'ID',
          isRequired: true,
          attributes: []
        },
        name: {
          name: 'name',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        },
        aspects: {
          name: 'aspects',
          isArray: true,
          type: {
            nonModel: 'Aspect'
          },
          isRequired: false,
          attributes: [],
          isArrayNullable: false
        },
        skills: {
          name: 'skills',
          isArray: true,
          type: {
            nonModel: 'Skill'
          },
          isRequired: false,
          attributes: [],
          isArrayNullable: false
        },
        stunts: {
          name: 'stunts',
          isArray: true,
          type: {
            nonModel: 'Stunt'
          },
          isRequired: false,
          attributes: [],
          isArrayNullable: false
        },
        stress: {
          name: 'stress',
          isArray: false,
          type: {
            nonModel: 'StressMap'
          },
          isRequired: true,
          attributes: []
        },
        fate: {
          name: 'fate',
          isArray: false,
          type: {
            nonModel: 'FatePoints'
          },
          isRequired: true,
          attributes: []
        },
        background: {
          name: 'background',
          isArray: false,
          type: 'String',
          isRequired: false,
          attributes: []
        },
        notes: {
          name: 'notes',
          isArray: false,
          type: 'String',
          isRequired: false,
          attributes: []
        },
        playerID: {
          name: 'playerID',
          isArray: false,
          type: 'ID',
          isRequired: false,
          attributes: []
        },
        player: {
          name: 'player',
          isArray: false,
          type: {
            model: 'Player'
          },
          isRequired: false,
          attributes: [],
          association: {
            connectionType: 'BELONGS_TO',
            targetNames: ['playerID']
          }
        },
        createdAt: {
          name: 'createdAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        },
        updatedAt: {
          name: 'updatedAt',
          isArray: false,
          type: 'AWSDateTime',
          isRequired: false,
          attributes: []
        },
        owner: {
          name: 'owner',
          isArray: false,
          type: 'String',
          isRequired: false,
          attributes: []
        }
      },
      syncable: true,
      pluralName: 'Characters',
      attributes: [
        {
          type: 'model',
          properties: {}
        },
        {
          type: 'key',
          properties: {
            name: 'byPlayer',
            fields: ['playerID']
          }
        },
        {
          type: 'auth',
          properties: {
            rules: [
              {
                allow: 'private',
                operations: ['create', 'update', 'delete', 'read']
              },
              {
                provider: 'userPools',
                ownerField: 'owner',
                allow: 'owner',
                identityClaim: 'cognito:username',
                operations: ['create', 'update', 'delete', 'read']
              }
            ]
          }
        }
      ]
    }
  },
  enums: {},
  nonModels: {
    Aspect: {
      name: 'Aspect',
      fields: {
        type: {
          name: 'type',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        },
        phrase: {
          name: 'phrase',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        }
      }
    },
    Skill: {
      name: 'Skill',
      fields: {
        name: {
          name: 'name',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        },
        rating: {
          name: 'rating',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        }
      }
    },
    Stunt: {
      name: 'Stunt',
      fields: {
        name: {
          name: 'name',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        },
        description: {
          name: 'description',
          isArray: false,
          type: 'String',
          isRequired: true,
          attributes: []
        }
      }
    },
    Stress: {
      name: 'Stress',
      fields: {
        total: {
          name: 'total',
          isArray: false,
          type: 'Int',
          isRequired: true,
          attributes: []
        },
        current: {
          name: 'current',
          isArray: false,
          type: 'Int',
          isRequired: true,
          attributes: []
        }
      }
    },
    StressMap: {
      name: 'StressMap',
      fields: {
        physical: {
          name: 'physical',
          isArray: false,
          type: {
            nonModel: 'Stress'
          },
          isRequired: true,
          attributes: []
        },
        mental: {
          name: 'mental',
          isArray: false,
          type: {
            nonModel: 'Stress'
          },
          isRequired: true,
          attributes: []
        }
      }
    },
    FatePoints: {
      name: 'FatePoints',
      fields: {
        refresh: {
          name: 'refresh',
          isArray: false,
          type: 'Int',
          isRequired: true,
          attributes: []
        },
        current: {
          name: 'current',
          isArray: false,
          type: 'Int',
          isRequired: true,
          attributes: []
        }
      }
    }
  },
  codegenVersion: '3.4.3',
  version: 'e59193e13f3cdab27b8cb28ac16b5409'
}
