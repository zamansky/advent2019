 
data  = [ int(x) for x in open("day02.dat").read().split(",")]

def run_program(program):
    ip = 0
    while True:
        op = program[ip]
        if op == 99:
            break
        a = program[ program[ip+1] ]
        b = program[ program[ip+2] ]
        dest = program[ip+3]
        if op == 1:
            program[dest] =  a + b
        else:
            program[dest] = a * b
        ip = ip + 4
    return program[0]

def part1(data):
    program = data[:]
    program[1]=12
    program[2]=2
    return run_program((program))


def part2(data):
    results = []
    for noun in range(100):
        for verb in range(100):
            program = data[:]
            program[1] = noun
            program[2] = verb
            r = run_program(program)
            results.append( (100*noun+verb, r))
    ans = [ x for x in results if x[1]==19690720]
    return ans
    
print(part1(data))
print(part2(data))

