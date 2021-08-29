<h1 align="center"> Squidly </h1> <br>

---

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

The most advanced Discord-Bot satisfying your competitive needs whilst playing [Paladins][pala]!

**Invite to your server [here](https://dotspace.dev/squidly/invite)**

## Features

A few of the things you can do with Squidly:

* Retrieve & display data directly from Hi-Rez
* Works with every Platform (Switch, Steam, Epic ...)
* Visualize opposing teams and showing their stats
* Show Level, Rank, Playtime and more rich information

## Feedback

Feel free to send us feedback on [Twitter](https://twitter.com/dotspaceDev)
or [file an issue](https://github.com/dotSpaceTeam/Squidly/issues/new). Feature requests are always welcome.  
Also feel free to submit a PR!  https://makeapullrequest.com/

If there's anything you'd like to chat about, please feel free to join our [Discord](https://discord.gg/mFfDMAEFWE)!

## Building and Running

#### Building

- To build and execute the jar **Java 16** is recommended.
- Execute ``gradlew build`` to create the executable fat jar.

#### Running

* You need **Java 16** to run this application.
* The following parameters are needed to sucessfully start the application.

##### Parameters

|Heres what you need!|
|---|
|Discord bot token   |
|Hi-rez developer ID |
|Hi-rez authentication key |

To provide these to the application you can simply use the environment variables:  
``SQUIDLY_BOTTOKEN``, ``SQUIDLY_DEVID`` and ``SQUIDLY_AUTHKEY``  

The following chart displays some of the possibilities to provide the
parameters.

|  Parameter | ↓ | ↓  | ↓ |  ↓ | ↓ |
|------------|---|----|---|----|---|
| bot token  |ENV|ENV |ARG|FILE|ANY|
| dev id     |ENV|JSON|ENV|JSON|ARG|
| auth key   |ENV|JSON|ENV|JSON|ARG|

``ENV``: The value stored in the appropriate environment variable.  
``ARG``: The value directly passed as an argument.  
``FILE/JSON``: The path to a file containing the value/s directly passed as an argument.

**You can mix them up, but be careful to:**

1. Never seperate the dev id and the auth key
2. always keep this order: ``token - (dev id - auth key)``  
   (You can remove either the token or the grouped part, if it is stored in environment variabels)

With ENV: ``java -jar Squidly.jar``  
With files: ``java -jar Squidly.jar /tokenfile /credentials.json``  
With args: ``java -jar Squidly.jar tokensample devid authkey``  

Sample json file:

```
{
 "devID": "1111",
 "authKEY": "SAMPLEKEYSAMPLEKEYSAMPLEKEY"
}
```

[pala]: <https://www.paladins.com/>

[hirez]: <http://www.hirezstudios.com/>

