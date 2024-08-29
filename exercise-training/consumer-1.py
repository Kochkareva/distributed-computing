import pika
import random
import threading
import time


def message_manager(channel, queue_name, exchange_name):
    channel.queue_declare(queue=queue_name)
    channel.queue_bind(exchange=exchange_name, queue=queue_name)

    def callback(ch, method, properties, body):
        task = body.decode()
        print(f" [x] Received : {task}")
        time.sleep(random.randint(2, 3))
        if task == "started exercise":
            print(" [x] Started timer")
        elif task == "finished exercise":
            print(" [x] Stopped timer, started another exercise")
        elif task == "started training":
            print(" [x] Started manage exercise")
        else:
            print(" [x] Result of training given")
        ch.basic_ack(delivery_tag=method.delivery_tag)

    channel.basic_consume(queue=queue_name, on_message_callback=callback)
    print("[*] Waiting for messages. To exit press CTRL+C")
    channel.start_consuming()


connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
exchange_name = 'logs'
queue_name = 'slow-queue'

consumer_thread = threading.Thread(target=message_manager, args=(channel, queue_name, exchange_name))
consumer_thread.start()
consumer_thread.join()
