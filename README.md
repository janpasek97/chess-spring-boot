# Five in a row APP - KIV/PIA 2020/21 semester project

This project was created as a semester project for KIV/PIA 2020/21. The project implements an online version of the famous game called "Five in a row".  

## Requirements

In the following lists, I state all implemented features separated into mandatory and bonus categories.
### Mandatory
- login screen
- registration screen
- lobby with a list of logged-in users
- friends list
- possibility to ask any on-line (and not currently engaged) user to play a game 
- gameplay
- one or a selection of limited size boards
- administration - user administration, password reset
- log of all game results

### Bonus
- an unlimited board
- password strength evaluation
- OAuth2 authentication using Google, Github and Facebook
- password recovery via an email link
- in-game chat
- HTML canvas for the gameplay

## Running the application

### Prerequisities
In order to build and run the application, one has to have installed the following SW:
- Maven
- Docker + Docker Compose

### Build & Run

The whole application can be run by using only 3 commands from the `root of the repository` (or from the `app` folder in the solution archive).

1. `mvn package` - builds the application into `target/FiveInARow.war`
2. `docker build -t pia/pasekj .` - build a docker image of the application
3. `docker-compose up -d` - runs the application including required database servers

After that, you can display the application on [localhost:8080](http://localhost:8080)

### Troubleshooting

In case the `docker build` command fails on message:
``` error checking context: 'can't stat 'xxx/data/fiveinarowdata''. ```
apply the following sequence of commands:

```bash
cd data
sudo chmod a+rwx -R fiveinarowdata/
cd ..
```

and build the image again.

## Developer's contact
In case any unexpected error occurs while running the application, please contact me on `pasekj{at}students.zcu.cz`.