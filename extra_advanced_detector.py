import sys
from collections import OrderedDict

filename = sys.argv[1]

if filename is None:
    print("Please specify filename")
    exit(1)

lines = open(filename, 'r').readlines() # Read it as a list of strings

results_unsorted = {}

for i in range(len(lines)):
    try:
        # [0] LIN-REMOVE-FALSE,
        # [1] 6325520880534,


        data = lines[i].split(",")
        time = int(data[1])
        lin, op, res = data[0].strip().split("-")

        results_unsorted[time] = {
            "op": op,
            "res": res
        }

    except Exception as e:
        continue

# Sort keys ascending
results = OrderedDict(sorted(results_unsorted.items()))

# Find failed contains after add:
invalid_count = 0
total = len(results.keys())

for i in range(total-1):
    cur = results[results.keys()[i]]
    nex = results[results.keys()[i+1]]

    if cur["op"] == "ADD" and  nex["op"] == "CONTAINS" and nex["res"] == "FALSE":
        invalid_count += 1


    elif cur["op"] == "REMOVE" and  nex["op"] == "CONTAINS" and nex["res"] == "TRUE":
        invalid_count  += 1


ratio = float(invalid_count) / float(total)

print("Total: ", total, ", invalid: ", invalid_count, ", ratio: " + str(ratio))

