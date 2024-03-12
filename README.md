## 隐岩砥凿
## Too Hard to Dig (2Hard2Dig)

### 简介
### Brief

受 [痒死唠](https://www.mcmod.cn/class/3206.html) 启发的一个高版本 Minecraft 模组


Inspired by [Yung's Law](https://www.curseforge.com/minecraft/mc-mods/yungs-law) , 2h2d is a mod dedicated for Minecraft 1.20+.

### 功能列表

- 玩家的挖掘速度受目标方块所在的 Y 坐标影响，且随 Y 值下降挖掘速度下降
- 挖掘速度下降随挖掘计数增加而从原速逐渐下降，并在超出一定阈值后达到最慢
- 当玩家两次挖掘的方块坐标之差超过 10 时重置挖掘计数（间距超过 10 挖掘计数重置为最小值的一半，Y值超过 10 时挖掘计数重置为最小值）
- 带有矿物标签的方块不计入挖掘计数，且挖掘速度不受影响
- 按生物群系配置功能开启的最大高度 (WIP)
- 按维度配置计入挖掘计数的方块和不计入挖掘计数的方块 (WIP)
- 挖掘速度达到最慢时继续挖掘有概率额外损耗工具耐久 (WIP)*
- 提高主世界矿物在矿洞表面生成的概率 (WIP)

_注 * ：仍在考虑是否添加该特性_ 

### Functions Implemented

- Player's break speed is related to the y pos of the targeted block, and as the y pos decreases, the breakspeed shrank too.
- Break speed will decrease only after the player mined enough blocks (12 for now), and will reach the minimum when mined 16 blocks.
- The dig count will be half (8 blocks) when two mine actions are 10 blocks away, while the y difference reaching 10 will reset the counter.
- Blocks with tag ORES won't trigger the dig counter, neither do the break speed will be affected.
- The feature is only enabled when the y pos is lower than 64 for now, and will be made configurable according to the biome in the future.