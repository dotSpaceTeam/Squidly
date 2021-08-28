<h1 align="center"> Squidly </h1> <br>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Feedback](#feedback)
- [Building and Running](#Building-and-Running)

## Introduction

![Java Version](https://img.shields.io/badge/Java-16-important?style=flat-square&logo=java)
![GitHub Workflow Status](https://img.shields.io/github/workflow/status/dotspaceteam/Squidly/Java%20CI%20with%20Gradle?style=flat-square)
![GitHub issues](https://img.shields.io/github/issues/dotspaceteam/squidly?style=flat-square)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)
[![Support Server](https://img.shields.io/discord/678733739504697375.svg?color=7289da&label=dotSpace%20Dev&logo=discord&style=flat-square)](https://discord.gg/mFfDMAEFWE)

The most advanced Discord-Bot satisfying your competitive needs whilst playing [Paladins][pala] or [Smite][smite]!

**Invite to your server [here](https://dotspace.dev/squidly/invite)**

## Features

A few of the things you can do with Squidly:

* Retrieve & display data directly from Hi-Rez 
* Works with every Platform (Switch, Steam, Epic ...) 
* Visualize opposing teams and showing their stats
* Show Level, Rank, Playtime and more rich information

## Feedback

Feel free to send us feedback on [Twitter](https://twitter.com/dotspaceDev) or [file an issue](https://github.com/dotSpaceTeam/Squidly/issues/new).
Feature requests are always welcome. <br>
Also feel free to submit a PR! https://makeapullrequest.com/

If there's anything you'd like to chat about, please feel free to join our [Discord](https://discord.gg/mFfDMAEFWE)!

## Building and Running

#### Building
- To build and execute the jar **Java 16** is recommended.
- Execute ``gradlew build`` to create the executable fat jar.

#### Running
- You need to specify two or three arguments to start the application.
- The first argument needs to be a path to a text file containing the discord token **or** the token itself.
- The second argument either needs to be a path to a json file containing the credentials of the Hi-Rez API or the Hi-Rez developerID.
- The third argument is only applicable if u specified the developerID before. You **need** to specify the authentication key.

Sample json file:
```
{
 "devID": "1111",
 "authKEY": "SAMPLEKEYSAMPLEKEYSAMPLEKEY"
}
```


[pala]: <https://www.paladins.com/>

[smite]: <https://www.smitegame.com/>

[hirez]: <http://www.hirezstudios.com/>

