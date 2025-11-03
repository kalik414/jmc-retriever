# JustMC data retriever
Utility to get information about players and worldsw located on JustMC.
## Usage
1. Download program from releases, put it to suitable diretory;
2. Open terminal there, execute following command:
```
$ java -jar jmcr.jar
```
**MAKE SURE** that you have Java 16 or newer.
## Commands
Generic commands as `help` or `exit` are not why are we here. Let's look at utility featured commands:
```
player [username]
```
To retrieve data about player, such as:
| Item | Description |
| :------: | :-----------------------: |
| Username | Not particulary helpful.. |
| Rank | Self-explanatory, i guess |
| Skin URL | Only prints if possible |

```
world [id]
```
To retrieve data about world by its ID. Whole bunch of data, to be more precise:
| Item | Description |
| :-------------: | :-----------------------------------: |
| ID | Don't you've just entered it..? |
| Owner | Somebody who created the world |
| Name | May have problems with charsets on Windows |
| Size | In blocks, or sometimes in chunks |
| Votes | They're resetting every month |
| Generator | Typically, "void" or "flat" |
| Spawn pos | Everything: x, y, z, pitch and yaw |
| Builders | List of builders separated with space |
| Developers | Same but for developers |
| Flyers | what a stupid name |
| Whitelist | Players with whole access |
| Banned | Blacklisted actually |
| Is world closed | Can or not you join it |
| Time | In ticks |
| Is build allowed | idk that is a thing |
| Is flight allowed | Same actually |
| Is physics allowed | Now i'm confused there |
| Creation time | Wierd timestamp |
| Is published | Can be done with 10 votes only |
| Is recommended | Fortune's duty |
| Categories | List of them |
| Resource pack | It's direct URL |

## Building and modifing
idk you're on your own here. Feel free to do anything with this piece of code.

## That's it
Hope you find this somewhat usefull.
