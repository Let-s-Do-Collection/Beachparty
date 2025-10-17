[2.1.2]

**Fixed**
- `Palm Leaves` loot table
- `Rubber Ring` texture not showing on NeoForge when worn in chest slot
- `Trunks` not showing rubber band texture

***

[2.1.1]

**Fixed**
- Crash when placing waterlogged `Seashells`

***

[2.1.0]

**Welcome to 1.21.1**

***

[2.0.2] 

**Added**
- Added pt_br translation (thanks fo Fabiano)

**Changed**
- `Message in a Bottle` now only spawns above blocks in the `minecraft:sand` block tag and only if the target block is empty

**Fixed**
- Fixed server crash caused by an infinite loop in the rubber ring trinket’s tick method by replacing the downward search with a vertical raycast
- Fixed crash (timeout) when player is teleported to unloaded blocks while wearing a rubber ring by checking if the block is loaded before performing the raycast (thanks to macuguita)

***

[2.0.1]

**Added**
- All Songs being played by the Radio can now also be found in Buried Treasured and collected
- Added an Advancement for collecting all Beachparty exclusive Music Discs

**Changed**
_no entries_

**Fixed**
- Reduced MessageInABottleSpawner block scans to avoid server hangs
- Missing Beachball Texture
- Missing Config Translations on Fabric
- Cleaned up duplicate contributors entries in the fabric.mod.json
- Reduced Zombies ApproachSandcastleGoal tick usage
- Exception caused when a Player on a Server wears a Rubber Ring and jumps into Water (Thanks to Christopher Stolworthy)
- RadioBlock: fixed sounds continuing after block was broken or chunk unloaded
- RadioBlock: fixed client disconnects caused by missing sound instances
- RadioBlock: fixed overlapping/stacking of multiple tracks at once

***

[2.0.0] 

### **New & Updated Features, Misc**
- Changed Release Channel to Beta
- Removed DoAPI
- Added a Beach Goal 
- Added a Beach Ball
- Added Advancements
- Palm Trees spawning now less frequently
- Beach Towels can now be used for sleeping but do not set a spawn point
- Completionist Banner has been added
- All Let's Do wood variants have windows – Palm Wood now as well!
- Most textures for palm wood, furniture, and decorative elements have been adjusted to look less modern
- The Beach Chair has been renamed to Hooded Beach Chair and now consists of two parts that can be vertically connected
- Other chairs have been slightly modified and renamed
- Palm saplings have been replaced with palm sprouts They no longer drop from leaves but can be obtained with a small chance (45%) by opening coconuts
- Coconuts can now regrow by using bone meal on palm leaves
- Throwing a coconut at a solid block now drops 2 open coconuts
- The MiniFridge can now be used to craft ice and other cold blocks/items like snow
- Sandcastles can now be reinforced by using water bottles Reinforced Sandcastles can't be destroyed by jumping on them 
- "Message in a Bottle" are now spawning while near water or beaches
- Deck Chair has been renamed to Sun Lounger – the backrest can now be adjusted, and it can be used for sleeping at night (without setting a spawn point)
- The FloatyBoat now has its own model
- Added new Music to the Radio
- SeaShells no longer have a CollisionShape
- Pool Noodle doesnt deal any damage anymore
- Zombies like Sandcastles! They will walk towards any SandCastle within 12 Blocks and try to trample it 
- Increased the crafting result for Palm Tables from "1" to "2"
- Increased the crafting result for Beach Chairs from "1" to "2"
- Beach Parasols now provide proper protection from heavy sunlight, reducing fire damage by 3%
- Beachparty now only affects beach biomes — cold beaches are no longer included
- All Beach Villager trades have been completely revamped
- Spawn rates in chest loot have been significantly reduced
- Added various structures that generate near or directly on beaches
- Most armor items no longer have effects The (OP) set bonuses have been removed
- Armor pieces are now artifacts that can be placed in Curios/Trinket slots to gain additional bonuses They can no longer be crafted and must be found in loot chests or be traded
- Armor pieces found in chests have random colors
- Sunglasses no longer use mixels
- Config for a few features Theres still a lot of potential though 
- Support for JEI Effect Description
- Support for Diet

### **Bug Fixes**
- Wheat can now be properly dried but has been renamed to Thatch and given a new texture
- Placing a hay bale in water (or using a water bottle on it) turns it into a wet hay bale
    - In hot biomes, it dries into thatch
    - In other biomes, it reverts back into a hay bale
    - A clay ball can prevent it from drying
    - Using a water bottle resets the drying process
- The radio no longer disconnects players in multiplayer 
- Tall Palm Torch now also drops when breaking the bottom part
- Sandwaves will now also spawn when using Forge

### **Beachparty 2.0.0: Overhaul**
This update introduces a refined version of Beachparty with adjusted textures, naming conventions, and reworked features

### **Why this rework?**
Currently, Beachparty feels like a collection of loosely connected beach-themed content rather than a cohesive experience While it does capture a general beach atmosphere, it lacks structure and fun and / or meaningful gameplay elements The removal of the DoAPI required a lot of refactoring and restructuring anyway, so i have bundled it together with this rework

This update aims to give Beachparty a more defined identity with unique and well-integrated features Not every addition needs to affect gameplay, but even decorative elements should serve a small functional purpose to make them more relevant within the game world

The goal is to create a more immersive, engaging, and enjoyable beach-themed experience where every feature feels intentional rather than just another random addition
