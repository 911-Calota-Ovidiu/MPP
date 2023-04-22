import multiprocessing
import psycopg2

def process_range(start, end):
    # Connect to the database
    conn = psycopg2.connect(database="MPP",
                        host="localhost",
                        user="postgres",
                        password="sa",
                        port="5432")
    cursor = conn.cursor()
    fid=0
    # Loop through the range and generate friends
    for i in range(start, end):
        for j in range(i, 999999):
            # Generating friends
            f1 = i
            f2 = j
            if f1!=f2:
            # Insert friend data into the database
                insertFriend = "INSERT INTO friend(id) VALUES(%s)"
                insertF1 = "INSERT INTO first_friend(childid,id) VALUES(%s,%s)"
                insertF2 = "INSERT INTO second_friend(childid,id) VALUES(%s,%s)"
                cursor.execute(insertFriend, ([fid]))
                cursor.execute(insertF1, (f1, fid))
                cursor.execute(insertF2, (f2, fid))

            # Increment the friend ID and commit the changes
            fid += 1
            conn.commit()

    # Close the database connection
    cursor.close()
    conn.close()

if __name__ == '__main__':
    # Define the number of processes to use and the range of the loop to divide among the processes
    num_processes = 4
    chunk_size = 250000
    ranges = [(i, i + chunk_size) for i in range(0, 999999, chunk_size)]

    # Set up the database connection parameters


    # Create a pool of worker processes
    with multiprocessing.Pool(num_processes) as pool:
        # Map the process_range function to each range
        pool.starmap(process_range, [(start, end) for start, end in ranges])