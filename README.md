# SH-Server
## Obligatory Badges
| Travis Integration | Code Coverage | License |
| ------------------ | ------------- | ------- |
| [![Build Status](https://travis-ci.org/nikmanG/SH-Server.svg?branch=master)](https://travis-ci.org/nikmanG/SH-Server) | [![Code Coverage](https://codecov.io/gh/nikmanG/SH-Server/branch/master/graph/badge.svg)](https://codecov.io/gh/nikmanG/SH-Server) | [![License: CC BY-NC-SA 4.0](https://licensebuttons.net/l/by-nc-sa/4.0/80x15.png)](https://creativecommons.org/licenses/by-nc-sa/4.0/) |

## What this is
This is a chat game version of [Secret Hitler](https://www.secrethitler.com/). Very beloved game at work and if you don't know what it is - it's esentially Mafia with slightly different rules
The rules for the original game (and what I am implementing the online version with) is [here](https://secrethitler.com/assets/Secret_Hitler_Rules.pdf).

## What the repo is
The SH-Server repo is for the server component. The client is located at [SH Client](https://github.com/nikmanG/SHClient).
The server uses the Java Socket protocol, currently non-encrypted. Was contemplating on using a main game loop instead but figured this was a nice challenge to try and make the game concurrent.

## Licensing
The original game is licensed under [CC SA–BY–NC 4.0](https://creativecommons.org/licenses/by-nc-sa/4.0/), so that's what I am going with too. Attribution is provided in the README, and will be on startup of game (once I get to it).

## Usage
Currently I have no releases or packages so have to run from terminal or IDE. First server then connect clients. That's about it for now

## Aim of project
This isn't really made to be anything that could be used on a lage scale, and while I will make tests, it is by no means perfectly sound. 
It is really just a pet project for me to get back into Java, learn some design patterns, and mess around with some technologies I think might be cool (Docker and maybe something further on for creating servers dynamically).

## Disclaimer
There is virtually no security in this, like as mentioned before data transfer isn't encrypted so middle-man attacks are an obvious issue. Use at own risk.

## Devlogs
I am going to try do some devlogs of this on YouTube and save details in [LOGS.md](LOGS.md). The videos will address the technical side while the Log page will just be a brief explanation of what the game state currently is.
