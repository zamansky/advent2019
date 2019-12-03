
dx = {"U":0,"D":0,"L":-1,"R":1}
dy = {"U":1,"D":-1,"L":0,"R":0}


def makemove(map,point,move,id,laststep):
    """
    simulate a single move like R3,L5 etc.
    1. set the starting point
    2. loop over the # of steps
       a. Calculate the next point
       b.If the point is not in our "map"
         add it along with the steps so far.
       c. If it is and it's not our ID, add our ID and update
          the steps so far, otherwise disregard this point and
          just move on to the next one 
    """
    dir,steps = move
    for s in range(1,steps+1):
        point = (point[0]+dx[dir],point[1]+dy[dir])
        if point not in map:
            map[point] = {"steps":laststep+s,"intersect":False,"ids":set([id])}
        elif id not in map[point]["ids"]: # point is present but this id isn't
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

# convert to a more useable form ("R",34) instead of "R34"
l1 = [ (x[0],int(x[1:])) for x in l1_raw.split(',')]
l2 = [ (x[0],int(x[1:])) for x in l2_raw.split(',')]

#  build our map
m = processline({},0,l1)
m2 = processline(m,1,l2)


# for part 1, pull out all the intersecting lines, calculate
# the manhattan distance of the point to the origin
# sort and take the smallest
part1 = sorted([abs(x[0])+abs(x[1]) for x in m if m[x]['intersect']==True])[0]
print("Part 1: "+str(part1))

# instead of distance look at least steps
part2 = sorted([m[x]['steps'] for x in  m2 if m2[x]['intersect']==True])[0]
print("Part 2: "+str(part2))
