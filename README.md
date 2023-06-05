# FATE Amplify

This project was created from this template: https://github.com/rgilks/aws-amplify-cljs-template

See the main README in that project.

## Setup

How I set this up from the cljstemplate

1. Rename the `api/cljstemplate` folder to `api/fate`
2. Rename the `auth/cljstemplatecc274de4` folder to `api/fatec274de4`
3. Delete everything in `team-provider-info.json`
4. Find and replace `cljstemplate` with `fate` especially check the /amplify.config files. eg project-config.json -> "projectName": "cljstemplate",
5. Run `amplify init`
6. For the environment name use something like `devfatea`. This is because later you might want a devfateb devfatec etc and have other projects in the same account.
7. Run `amplify push`
8. Agree to the questions asked.
9. Run `yarn`
10. Run `yarn watch` `yarn webpack` `yarn karma` in separate terminals. The karma tests should pass
11. In the Amplify Console got to the app, click Hosting environments, select GitHub, give amplify the right to eccess the new repo in git, connect the main branch

It would be good to create a script to do this.
