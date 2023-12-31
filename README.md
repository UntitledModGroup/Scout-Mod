# Scout Releaded
[Modrinth](https://modrinth.com/mod/scoutreloaded)

A mod focused on "physically" extending the inventory (adding slots) through wearing various types of bags. Heavily inspired by makamys' [Satchels](https://github.com/makamys/Satchels).

![Preview image of inventory with a pouch, an upgraded pouch, and upgraded satchel equipped](.assets/inventory_preview.png)

Items are retained on the bag items themselves. The bag items are equipped through [Trinkets](https://github.com/emilyploszaj/trinkets) slots. A new slot type for pouches is added, with two slots.

## Future Plans
- [x] Fixing Repository gradle repos
- [ ] Getting a build from this repo
- [ ] Splitting up versions based on branches
- [ ] Satchel renders on the wearer
- [ ] Updating to 1.20.1

## Credits
* makamys - Original inspiration from Satchels
* Emi - Trinkets
* Kat - Original pouch texture, taken from an older mod of mine and reworked for Scout
* UTMG - Upgrading original source, (Reloaded Edition)

## Building
Due to using multi-projects in Gradle and the 1.19 version depending on the 1.18 version, you will get errors trying to build normally the first time.

1. Go into `settings.gradle` and comment out `include 'platform-1.19'`
2. Build the 1.18 version
3. Uncomment the include
4. You can now build the 1.19 version

Repeat everytime version is bumped.

## Intentions
My intentions are to keep this mod alive, I wish to update and add features later on.
## Gradle Split
I am hoping with this split it will remove the need for hacky and janky workaround for building it.
## No Forge plans still?
No. after rethinking and seeing, it would be better overall to make it from scratch for Forge due to the difference in "Trinket" mods. Forge uses Curios, where Fabric uses Trinkets.
