// WARNING: DO NOT EDIT!
// THIS FILE WAS GENERATED BY SHADOW-CLJS AND WILL BE OVERWRITTEN!

var ALL = {}
ALL['@testing-library/react'] = require('@testing-library/react')
ALL[
  '@tiptap/extension-collaboration'
] = require('@tiptap/extension-collaboration')
ALL['@mui/material/styles'] = require('@mui/material/styles')
ALL['react-dom/client'] = require('react-dom/client')
ALL['use-sync-external-store/shim'] = require('use-sync-external-store/shim')
ALL['aws-exports'] = require('aws-exports')
ALL['@tiptap/core'] = require('@tiptap/core')
ALL['@aws-amplify/ui-react'] = require('@aws-amplify/ui-react')
ALL['aws-amplify'] = require('aws-amplify')
ALL['@mui/material'] = require('@mui/material')
ALL['@mui/icons-material'] = require('@mui/icons-material')
ALL['react-dom'] = require('react-dom')
ALL['y-webrtc'] = require('y-webrtc')
ALL['yjs'] = require('yjs')
ALL['react-router-dom'] = require('react-router-dom')
ALL['@rjsf/validator-ajv6'] = require('@rjsf/validator-ajv6')
ALL['@rjsf/mui'] = require('@rjsf/mui')
ALL['models'] = require('models')
ALL['react'] = require('react')
ALL['@tiptap/react'] = require('@tiptap/react')
ALL['react-div-100vh'] = require('react-div-100vh')
ALL['@tiptap/starter-kit'] = require('@tiptap/starter-kit')
global.shadow$bridge = function shadow$bridge(name) {
  var ret = ALL[name]

  if (ret === undefined) {
    throw new Error(
      'Dependency: ' +
        name +
        ' not provided by external JS. Do you maybe need a recompile?'
    )
  }

  return ret
}

shadow$bridge.ALL = ALL
