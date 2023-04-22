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
addresses = ['Alba_Iulia', 'Cluj_Napoca', 'Bucuresti', 'Braila']
members = 2

for i in range(2, 1000000):
    # generating dad and mom
    dad = random.randint(0, 999999)
    mom = random.randint(0, 999999)

    # generating address
    address = random.choice(addresses)

    # generating members
    mbs = members
    if members == 5:
        members = 2

    insertQ = "insert into family(famid,mom,dad,nrofmembers,address) values(%s,%s,%s,%s,%s)"
    cursor.execute(insertQ, (i, mom, dad, mbs, address))
    conn.commit()
cursor.execute("SELECT * FROM adults")
conn.close()