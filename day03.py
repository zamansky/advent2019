
dx = {"U":0,"D":0,"L":-1,"R":1}
dy = {"U":1,"D":-1,"L":0,"R":0}

deltas = {"U":(0,1),"D":(0,-1),"L":(-1,0),"R":(1,0)}

def makemove(map,point,move,id,laststep):
    dir,steps = move
    for s in range(1,steps+1):
        point = (point[0]+dx[dir],point[1]+dy[dir])
        if point not in map:
            map[point] = {"steps":laststep+s,"intersect":False,"ids":set([id])}
        elif id not in map[point]["ids"]:
            map[point] = {"steps": map[point]["steps"]+laststep+s,"intersect":True,"ids":map[point]["ids"].union([id])}
    return (map,point,laststep+s)

def processline(map,id,line):
    point=(0,0)
    laststep = 0
    for m in line:
        (map,point,laststep) = makemove(map,point,m, id, laststep)
    return map



f = open("day03.dat")
l1_raw  = f.readline()
l2_raw = f.readline()

l1 = [ (x[0],int(x[1:])) for x in l1_raw.split(',')]
l2 = [ (x[0],int(x[1:])) for x in l2_raw.split(',')]

m = processline({},0,l1)
m2 = processline(m,1,l2)

part1 = sorted([abs(x[0])+abs(x[1]) for x in m if m[x]['intersect']==True])[0]
print(part1)
part2 = sorted([m[x]['steps'] for x in  m2 if m2[x]['intersect']==True])[0]
print(part2)
