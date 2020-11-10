import random;

def generate_data(filename, numChannels, x_bar_success, x_bar_failure, std):
    fi = open(filename, "w")
    for i in range(numChannels):
        s = x_bar_success + random.uniform(-1*std, std)
        f = x_bar_failure + random.uniform(-1*std, std)
        if(i != numChannels-1):
            fi.write(str(s) + "," + str(f) + ",")
        else:
            fi.write(str(s) + "," + str(f))
    fi.close()
    return f

#generate_data("test1.csv", 100, 96, 65, 10)

def generate_sequential_data():
    points = 32
    while points <= 1024:
        s = .60
        f = .60
        std = random.uniform(0, .05)
        while s<=.90:
            f_name = str(points) + "_" + str(round(s, 2)) + "_" + str(round(f, 2))
            generate_data(f_name, points, s, f, std)
            s += .05
            f += .04
        points = points << 1

generate_sequential_data()