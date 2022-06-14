## Learn Unity (Creating a 2D Game)

- [YouTube Tutorial Link](https://www.youtube.com/watch?v=gB1F9G0JXOo)

- [Download assets from here (links in YouTube tutorial seem broken)](https://github.com/alirioangel/monster-chase-game/tree/master/Assets) 

### Unity IDE Basics

- Can save layouts

- Hierarchy:

  - Where all your game objects are stored
  
  - every part of unity project is essentially made up of game object
  
  - for every game object, you add components/scripts to make them do stuff (collisions and shit)

- Inspector:
  
  - every component added to a selected game object

- Project:

  - all the folders and files you have in your unity project

- Scene:
  
    - location/position of all your game objects

- Game:
  
    - how its going to be viewed on actual device
  
    - Resolutions depends on device type which can be configured on `file -> build settings`

- Console:
  
    - debugging/logs/print-statements

- ***Asset Store (DEPRACATED)***

  - Moved to package manager

- Window -> Package manager

  - a lot of good and free game builders here!

- Animation

  - Create animations

- Animator

  - Link animations together

- Other tabs (window -> all za tabs, General, Rendering etc.)

### Unity Dev Basics:

- Sprite Sheet (list of 'state's/'animation's of the Sprite)

- To render sprite properly, need to add `Sprite Renderer` component

  - Click on the character image
  
  - Set Sprite mode to `multiple`
  
  - Click on Sprite editor
  
  - Do automatic slicing (if inaccurate, can outline the border yourself!)

- Add gravity/physics to game object (Rigid Body 2D)

- Colliders

  - XXXX Collider is for 3D
  
  - XXXX Collider 2D is for 2D
  
  - Need to add colliders to both elements if you want them to collide
  
  - `isTrigger` is like a toggle for collision
  
    - can be leveraged on programmatically for our game

- Audio Source
 
  - Allows us to play games and shit

- UI Elements

  - Any UI element is a child element of `Canvas`
  
  - Set `render mode` to `Screen Space - Camera` and use the default camera

- Kinematic mode in Rigidbody allows the gameobject to be subjected by physics but not gravity

### C# Programming Basics

- Language: C Hashtag

- Set editor to visual studio

  - Mac: `Preferences -> External Tools`
  
  - Windows: `Edit -> Preferences -> External Tools`

### Other Unity Things

- ```
  The file will have its original line endings in your working directory
  warning: LF will be replaced by CRLF in Unity Tutorial/Library/LibraryFormatVersion.txt.
  ```

  - [Forum](https://forum.unity.com/threads/warning-lf-will-be-rep-laced-by-crlf-in-project-file.822858/)

  - [Unity .gitignore file](https://github.com/github/gitignore/blob/master/Unity.gitignore)
  
  - [What you can & can't ignore when using Github for source control](https://forum.unity.com/threads/using-github.613558/)

- add `[Serializable]` to a private attribute to allow the attribute to stay private but accessible and configurable from the IDE

- add `[HideInInspector]` to a public attribute to hide the attribute while staying public

- Declaring a property does not mean you can immediately use it (you will get NullExceptionError if you do not reference it)

  - how to reference?
  
    1. Drag and drop on IDE
    
    2. `.GetComponent<Class>()`

### Animations Things

- Add animator to game object

- Create animator controller 

- Add animator controller to animator of game object

- Open the animation tab 

  `window -> animation -> animation`

- Create animation

  - to create more animations

    `under preview click on the name of the animation -> create new clip`

- Drag sprite sheet "frames" into the animation bar

  - demo the animation by going to the scene where you should be able to press play on the animation tab

- click 3 dots on animation tab to configure animation settings (seconds/frames, fps etc.)

  - you can also click on the animation itself and configure the speed of the aimations

- To transit between the different animations, go to animator tab

  `right click on an animation -> make transition ` 

  - You can also make conditions for transitions between animations

    `parameters -> add new parameter (bool, str, int etc.) -> conditions -> set to true/false`

    when walk parameter is true then transit from idle to walk, when walk parameter is false then transit from walk to idle, with walk parameter default value being false

  - you can configure the delay between transitions under animator's settings (set to 0)

### Camera follow

- set the position of the camera to the position of the player

- to avoid jittering effect, use LateUpdate instead of Update

  - LateUpdate updates the gameobject only after all calculations are done

### Sorting & Order layer

- order in which game objects are rendered

### Build/Deployment things

- Make sure you have installed the platform module you wish to build to

  `unity hub -> installs -> right click -> Add Modules`

- How to build?

  `file -> build settings -> target platform -> build`

- If you are building a mac os package on a windows computer, that is fine but you need to follow the following steps:
  
  - target platform: macOS
  
  - Architecture: Intel 64-bit + Apple Silicon
  
  - Unzip using ["The Unarchiver"](https://apps.apple.com/us/app/the-unarchiver/id425424353?mt=12)
  
  - https://stackoverflow.com/questions/50577473/unity-game-compiled-in-windows-doesn-t-open-in-mac

