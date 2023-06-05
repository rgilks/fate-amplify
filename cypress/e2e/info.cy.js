const cypressConfig = Cypress.env('CONFIG')

const {testUsers} = cypressConfig

const username = 'testUser1'
const {email, password} = testUsers[username]

describe('Info Page', () => {
  beforeEach(() => {
    cy.visit('/')
    cy.get('input[name="username"]').type(username)
    cy.get('input[name="password"]').type(password)
    cy.get('button[type="submit"]').click()
    cy.findByTestId('logged-in').contains('YOU ARE LOGGED IN!')
    cy.visit('/info.html')
  })

  it('Has a title', () => {
    cy.title().should('include', 'APP INFO')
    cy.get('h1').contains('APP INFO')
    // Add test for content when there is some..
  })

  it('Has a button leading to /games', () => {
    cy.get('.button').contains('ENTER').click()
    cy.url().should('include', '/games')
  })
})
