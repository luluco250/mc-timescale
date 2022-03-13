# Timescale mod for Minecraft

This mod slows down the passage of time in Minecraft, allowing days to last
longer and be more productive. Sunsets will also appear for more than a few
seconds.

Note that it does not affect the speed of ticks, only the daylight cycle will be
slowed down.

It is implemented entirely server-side, so players don't need to download it in
a multiplayer server.

The configuration is pretty simple, you have two values: a delay and stride.
- Delay sets how many ticks are waited before the time of day advances.
- Stride sets how many ticks are advanced between each delay.

The reason for these two parameters is due to Minecraft using integer values for
advancing day time, it's not possible to do fractional increments, so the mod
makes use of the delay followed by stride (where in the time is advanced
smoothly) to achieve the same effect. It is based on how time advances in Elder
Scrolls and Fallout games from Bethesda.

The default configuration is `delay=80` `stride=24`, which makes the daylight
cycle run at 30% the vanilla speed. You can check the current configuration with
`/timescale` or change it with `/timescale <delay> <stride>`. Both versions of
the command will also calculate the current timescale percentage.

The mod configuration can also be found in
`.minecraft/config/timescale.properties`, which uses a simple ini-like format
(Java's properties file format).
