# GoodCitizen App

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
GoodCitizen is an app that will inform the user on civic information relating to them in order to help them be a better citizen. The app will provide information on their delegates/representatives, locations of nearest ballots, and upcoming elections.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Political Informational
- **Mobile:** You can see your representatives, polling locations closest to you, and upcoming elections for which you can set notifications to remind you to participate
- **Story:** The Good Citizen app allows users to be informed of who represents them and how they can contribute as a voting citizen, making it an easy hub to find all the information regarding elections and where they can participate in them
- **Market:** The voting American public
- **Habit:** User can scroll through the elections list and find polling locations around them
- **Scope:** This product will begin with just an informational hub for voting citizens, but can acquire many features such as emailing/texting a representative from the app with popular templates about issues,  sharing information with friends, and creating notifications from the app for reminders of voting deadlines

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* [ ]User can log-in/sign up with an account
* [ ]User can create a profile picture with the camera
* [ ]User can set their address in their account (disclaimer that the address is only used to give them their civic information)
* [ ]User can get a list of representatives from national to local level
* [ ]User can sort the list of representatives by levels
* [ ]User can get a list of elections, sorted in upcoming order
* [ ]User can get a list of polling locations near them
* [ ]User can sort the list of polling locations by locations or by hours open
* [ ]User can view and edit their address to update results

**Optional Nice-to-have Stories**

* [ ]User can view the polling locations as a map
* [ ]User can share elections with their friends on the app
* [ ]User can set notifications to remind them of elections
* [ ]User can view the information even when they don't have data (save information in a database)
* [ ]User can click on items in scrollable lists to open a detailed view
* [ ]User can open representative's social medias and send them emails/texts
* [ ]

### 2. Screen Archetypes & Navigation
* Login Screen
   * Profile picture (if exists)
   * Name & Password
   * Login Button (to Home Page)
   * Sign in Button (to Sign Up screen)
* Sign Up Screen
   * Profile Picture (camera)
   * Name
   * Password
   * Address
   * Sign Up button (to Home Page)
* Home Page - Upcoming Elections
   * Recycler View
   * Dropdown to sort options
* Polling Locations Page
   * Recycler View
   * Dropdown to sort options
* Representatives Page
   * Recycler View
   * Dropdown to sort options
* Account Details
   * Profile Picture (camera)
   * Name (non-editable?)
   * Address
   * Save Changes button (disables when no new changes)
   * Go Back button on Action Bar (to previous page)
* [Potential Detail Pages]
   * Representative details
   * Election details
   * Map for locations

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* [fill out your first tab]
* [fill out your second tab]
* [fill out your third tab]

**Flow Navigation** (Screen to Screen)

* [list first screen here]
   * [list screen navigation here]
   * ...
* [list second screen here]
   * [list screen navigation here]
   * ...

## Wireframes
Basic Mock-up
https://www.fluidui.com/editor/live/project/p_IV0yzIbbtpBRXtPFJa9dsanKbfMztE0W
More Detailed Wireframes:


### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- API: Google Civic Information API
