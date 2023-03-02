import copy
from pack.queue import Queue
from global_variable import ant_num, Q, RHO, weight, loss_value, BETA, ALPHA
from models import CourierInformationModel
import random
import json

que = Queue()
shortest_distance = 0
str1 = ''
distance_graph = [[]]
pheromone_graph = [[]]
distance_x = []
distance_y = []
city_num = 0

def initialization():
    global distance_graph, pheromone_graph, city_num, que,shortest_distance,str1,distance_x,distance_y, courier_information
    que = Queue()
    shortest_distance = 0
    str1 = ''
    distance_graph = [[]]
    pheromone_graph = [[]]
    distance_x = []
    distance_y = []
    city_num = 0
    courier_information = CourierInformationModel.query.all()
    for i in range(len(courier_information)):
        distance_x.append(courier_information[i].address_x)
        distance_y.append(courier_information[i].address_y)
    city_num = len(distance_x)
    distance_graph = [[0.0 for i in range(city_num)] for j in range(city_num)]  # 列表解析 初始化城市距离为0
    # 信息素
    pheromone_graph = [[1.0 for i in range(city_num)] for j in range(city_num)]  # 初始化信息素为1
    return courier_information




def new():
    global ants, run, temp_pheromone, best_ant, iter
    # 求城市间的距离
    for i in range(city_num):
        for j in range(city_num):
            temp_distance = pow((distance_x[i] - distance_x[j]), 2) + pow((distance_y[i] - distance_y[j]), 2)
            temp_distance = pow(temp_distance, 0.5)
            distance_graph[i][j] = float(int(temp_distance + 0.5))  # 向下取整；+0.5实现四舍五入
    # 初始化信息素
    for i in range(city_num):
        for j in range(city_num):
            pheromone_graph[i][j] = 1.0

    ants = [Ant(ID) for ID in range(ant_num)]  # 初始蚁群
    best_ant = Ant(-1)  # 记录最优解蚂蚁
    best_ant.total_distance = 99999  # 初始最大距离
    run = True
    iter = 0


# @app.route('/api/json', methods=['GET', 'POST'])
def search_path():
    global ants, run, temp_pheromone, best_ant, iter, best_path, shortest_distance,que,weight,loss_value,str1
    loss_value = 0

    if (iter >= 15):
        que.put(int(best_ant.total_distance))
        for i in range(1 , que.size()):
            loss_value =loss_value + weight*(que.find(i-1) - que.find(i))
        que.get()
        print(loss_value)
        if(loss_value <= 50):
            return str1, shortest_distance
        
    else:
        que.put(int(best_ant.total_distance))

    # 遍历每一只蚂蚁
    for ant in ants:
        # 搜索一条路径
        ant.search_path()
        # 与当前最优蚂蚁比较
        if ant.total_distance < best_ant.total_distance:
            # 更新最优解
            best_ant = copy.deepcopy(ant)
            best_path = best_ant.path
            shortest_distance = int(best_ant.total_distance)

    # 更新信息素
    temp_pheromone = [[0.0 for col in range(city_num)] for raw in range(city_num)]
    for ant in ants:
        for i in range(1, city_num):
            start, end = ant.path[i - 1], ant.path[i]
            # 在路径上的每两个相邻城市间留下信息素，与路径总距离反比，蚁周模型
            temp_pheromone[start][end] += Q / ant.total_distance
            temp_pheromone[end][start] = temp_pheromone[start][end]

    # 更新所有城市之间的信息素，旧信息素衰减加上新迭代信息素
    for i in range(city_num):
        for j in range(city_num):
            pheromone_graph[i][j] = pheromone_graph[i][j] * RHO + temp_pheromone[i][j]
    iter = iter + 1
    print(u"迭代次数：", iter, u"最佳路径总距离：", int(best_ant.total_distance))
    print(best_path)
    
    # json(best_path,shortest_distance)
    str1 = processing_data()
    return str1, shortest_distance

def search_path_for():
    global ants, run, temp_pheromone, best_ant, iter, best_path, shortest_distance,que,weight,loss_value,str1
    for i in range(2000):
        loss_value = 0

        if (iter >= 200):
            que.put(int(best_ant.total_distance))
            for i in range(1 , que.size()):
                loss_value =loss_value + weight*(que.find(i-1) - que.find(i))
            que.get()
            # print(loss_value)
            if(loss_value <= 1):
                return str1, shortest_distance
            
        else:
            que.put(int(best_ant.total_distance))

        # 遍历每一只蚂蚁
        for ant in ants:
            # 搜索一条路径
            ant.search_path()
            # 与当前最优蚂蚁比较
            if ant.total_distance < best_ant.total_distance:
                # 更新最优解
                best_ant = copy.deepcopy(ant)
                best_path = best_ant.path
                shortest_distance = int(best_ant.total_distance)

        # 更新信息素
        temp_pheromone = [[0.0 for col in range(city_num)] for raw in range(city_num)]
        for ant in ants:
            for i in range(1, city_num):
                start, end = ant.path[i - 1], ant.path[i]
                # 在路径上的每两个相邻城市间留下信息素，与路径总距离反比，蚁周模型
                temp_pheromone[start][end] += Q / ant.total_distance
                temp_pheromone[end][start] = temp_pheromone[start][end]

        # 更新所有城市之间的信息素，旧信息素衰减加上新迭代信息素
        for i in range(city_num):
            for j in range(city_num):
                pheromone_graph[i][j] = pheromone_graph[i][j] * RHO + temp_pheromone[i][j]
        iter = iter + 1
        print(u"迭代次数：", iter, u"最佳路径总距离：", int(best_ant.total_distance))
        print(best_path)
        # json(best_path,shortest_distance)
        str1 = processing_data()
    return str1, shortest_distance
    


def processing_data():
    global courier_information
    list_json = []
    str1 = ''
    api_json = {"x": 1, "y": 2, "num":0, "name": "2", "sort": -1}
    for i, j in zip(distance_x, distance_y):
        api_json["x"] = i
        api_json["y"] = j
        for ci in  courier_information:
            if ci.address_x == i and ci.address_y == j:
                api_json["name"] = ci.name
                api_json["num"] = ci.number
        list_json.append(copy.deepcopy(api_json))

    for i, j in zip(best_path, range(len(distance_x))):
        list_json[i]["sort"] = j
    n = len(list_json)
    for i in range(n):
        # 每次循环把未排序部分的最大元素移到最后
        for j in range(0, n-i-1):
            if list_json[j]['sort'] > list_json[j+1]['sort']:
                # 交换相邻的两个元素
                list_json[j], list_json[j+1] = list_json[j+1], list_json[j]        
    # print(list_json)
    # for i in list_json:
    #     str1=str1 + str(i) + '?'
    return list_json

def outdata():
    global courier_information
    list_json = []
    str1 = ''
    api_json = {"id": 1, "x": 1, "y": 2, "num":0, "name": "2"}
    for i, j in zip(distance_x, distance_y):
        api_json["x"] = i
        api_json["y"] = j
        for ci in  courier_information:
            if ci.address_x == i and ci.address_y == j:
                api_json["id"] = ci.id
                api_json["name"] = ci.name
                api_json["num"] = ci.number
        list_json.append(copy.deepcopy(api_json))
    return list_json

class Ant(object):

    def __init__(self, ID):
        self.ID = ID  # ID
        self.initialization_data()

    # 初始化数据
    def initialization_data(self):

        self.path = []  # 当前蚂蚁的路径
        self.total_distance = 0.0  # 当前路径的总距离
        self.move_count = 0  # 移动次数
        self.current_city = -1  # 当前停留的城市
        self.city_state = [True for i in range(city_num)]  # 城市状态 未去过为True

        city_index = random.randint(0, city_num - 1)  # 随机初始出生点
        self.current_city = city_index  # 出生点记录到当前城市
        self.path.append(city_index)  # 将当前城市添加到路径中
        self.city_state[city_index] = False  # 将该城市状态改为False
        self.move_count = 1

    # 选择城市
    def choice_next_city(self):
        next_city = -1
        select_citys_prob = [0.0 for i in range(city_num)]  # 存储去下个城市的概率 初始值为0
        total_prob = 0.0

        for i in range(city_num):
            if self.city_state[i]:
                select_citys_prob[i] = pow(pheromone_graph[self.current_city][i], ALPHA) * pow(
                    (1.0 / distance_graph[self.current_city][i]), BETA)
                total_prob += select_citys_prob[i]

        # 轮盘选择
        # 产生一个随机概率,范围0.0-total_prob
        if total_prob > 0.0:
            temp_prob = random.uniform(0.0, total_prob)
            for i in range(city_num):
                if self.city_state[i]:
                    temp_prob -= select_citys_prob[i]
                    if temp_prob < 0.0:
                        next_city = i
                        break
        if next_city == -1:
            next_city = random.randint(0, city_num - 1)
            while not (self.city_state[next_city]):  # if==False,说明已经遍历过了
                next_city = random.randint(0, city_num - 1)

        # 返回下一个城市序号
        return next_city

    # 计算路径总距离
    def cal_total_distance(self):
        # 总长度
        temp_distance = 0.0
        # 计算从0到city_num的距离
        for i in range(1, city_num):
            start, end = self.path[i], self.path[i - 1]
            temp_distance += distance_graph[start][end]

        # 加上最后一个到0的距离
        end = self.path[0]
        temp_distance += distance_graph[start][end]
        self.total_distance = temp_distance

    def move(self, next_city):
        self.path.append(next_city)  # 将下一个城市加到路径中
        self.city_state[next_city] = False  # 将该城市状态改为False
        self.total_distance += distance_graph[self.current_city][next_city]  # 城市距离加上到下一个城市的距离
        self.current_city = next_city  # 移动到下一个城市
        self.move_count += 1  # 移动次数加一

    def search_path(self):
        # 初始化
        self.initialization_data()

        # 搜索路径
        while self.move_count < city_num:
            # 移动到下一个城市
            next_city = self.choice_next_city()
            self.move(next_city)
        self.cal_total_distance()
