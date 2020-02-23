import json
from pprint import pprint

trip = False
classmap = {"client": {}, "server": {}}

with open("classes.csv", "r") as csv:
    for line in csv.readlines():
        if not trip:
            trip = True
        else:
            line = line.replace("\n", "").replace("\r", "")
            parts = line.replace("\"", "").split(",")

            side = ""
            if parts[4] == "0":
                side = "client"
            else:
                side = "server"

            classmap[side][parts[1]] = {
                "£name": parts[3] + "/" + parts[0]
            }

trip = False

pprint(classmap)

with open("fields.csv", "r") as csv:
    for line in csv.readlines():
        if not trip:
            trip = True
        else:
            line = line.replace("\n", "").replace("\r", "")
            parts = line.replace("\"", "").split(",")

            side = ""
            if parts[8] == "0":
                side = "client"
            else:
                side = "server"

            classmap[side][parts[6]][parts[2]] = {
                "type": "f",
                "name": parts[1],
                "sig": parts[4]
            }

trip = False

with open("methods.csv", "r") as csv:
    for line in csv.readlines():
        if not trip:
            trip = True
        else:
            line = line.replace("\n", "").replace("\r", "")
            parts = line.replace("\"", "").split(",")

            side = ""
            if parts[8] == "0":
                side = "client"
            else:
                side = "server"

            classmap[side][parts[6]][parts[2]] = {
                "type": "m",
                "name": parts[1],
                "sig": parts[4]
            }


with open("out.json", "w") as file:
    file.write(json.dumps(classmap, indent=4, sort_keys=True))

lines = []
with open("out.tinyfilev2", "w") as file:
    lines.append("tiny	2	0	intermediary	named\n")
    for classname, classobj in classmap["client"].items():
        lines.append("c	" + classname + "	" + classobj["£name"] + "\n")
        for mfname, mfobj in classobj.items():
            if mfname != "£name":
                if mfobj["type"] == "f" and mfname.upper() != mfname:
                    lines.append("	f	" + mfobj["sig"] + "	" + mfname + "	" + mfobj["name"] + "\n")
                elif mfobj["type"] == "m" and mfname.upper() != mfname:
                    lines.append("	m	" + mfobj["sig"] + "	" + mfname + "	" + mfobj["name"] + "\n")

    file.writelines(lines)
