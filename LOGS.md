# October 2019
## 0.0.1 [[VIDEO]](https://youtu.be/Zsqq53stDzQ)
### Current State
 glorified chat app with command abilities.

### Notes
- this was mostly setup stuff. Getting the project together mainly, next steps will be more about actual game design... I hope.

## 0.0.2
### Current State
Still an app with more command abilities. There are definite components of the game existing, but they are not joined together.
Version _0.0.4_ should be first where everything gets glued together.

### Notes
- One major delivery change was to emphasise that the server is really only logic so will send JSON instead of strings back to client, and let client do rendering.
- Still it isn't really a _game_. While it will have commands and majority of the abilities, there is still a lack of order.
- Need to add checks for special actions to make sure they are being performed at correct times.

## 0.0.3
### Current State

### Notes
- Added a [TARGETS.md](TARGETS.md) file that lays out what I want to achieve in this version. 
This should prevent the 0.0.2 issue of sporadic features getting added but having more structure.
This is in lieu of a proper tickets system that for a one man project may not be worth it.