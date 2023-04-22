import psycopg2 as ps
import random

conn = ps.connect(database="MPP",
                  host="localhost",
                  user="postgres",
                  password="sa",
                  port="5432"
                  )
cursor = conn.cursor()

# cursor.execute("SELECT * FROM adults")
# print(cursor.fetchmany(size=2))
people_names = ['Maria', 'Gigel', 'Goku', 'Bruce', 'Obu', 'Gabriel', 'Roxana', 'Mia', 'Geta']
nr_for_each = [1, 1, 1, 1, 1, 1, 1, 1, 1]
dates = ['28-03-1999', '28-03-2009', '28-03-2019']
colors = ["red", "green", "blue", "yellow", "purple"]
addresses = ['Alba_Iulia', 'Cluj_Napoca', 'Bucuresti', 'Braila']
overall_age = 10
for i in range(1000000):
    # generating name
    loc = random.randint(0, 8)
    name = people_names[loc] + "_" + str(nr_for_each[loc])
    nr_for_each[loc] += 1

    # generating age
    person_age = overall_age
    overall_age += 1
    if overall_age > 60:
        overall_age = 20

    # generating birthday
    bday = random.choice(dates)

    # generating address
    address = random.choice(addresses)

    # generating eye color
    eyes = random.choice(colors)

    person = str(i) + "," + name + "," + str(person_age) + "," + bday + "," + address + "," + eyes
    # print(person)
    insertQ = "insert into adults(adultid,aname,age,birthdate,address,eyecolor) values(%s,%s,%s,%s,%s,%s)"
    cursor.execute(insertQ, (i, name, person_age, bday, address, eyes))
    conn.commit()
cursor.execute("SELECT * FROM adults")
conn.close()