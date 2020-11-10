import random;

def generate_data(filename, numChannels, x_bar_success, x_bar_failure, std):
    f = open(filename, "w")
    for i in range(numChannels):
        f.write(str(x_bar_success + random.uniform(-1*std, std)) + "," + str(x_bar_failure+random.uniform(-1*std, std)))
    f.close()
    return f

#generate_data("test1.csv", 100, 96, 65, 10)

def generate_sequential_data():
    points = 32
    while points <= 1024:
        s = 60
        f = 60
        std = random.uniform(0, 5)
        while s<=90:
            f_name = str(points) + "_" + str(s) + "_" + str(f)
            generate_data(f_name, points, s, f, std)
            s += 5
            f += 4
        points = points << 1

generate_sequential_data()