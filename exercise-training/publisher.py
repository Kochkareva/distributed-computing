import pika
import random
import time

logs = ["started exercise", "finished exercise", "started training", "finished training"]

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
exchange_name = 'logs'
channel.exchange_declare(exchange=exchange_name, exchange_type='fanout')
print(' [*] Started. To exit press CTRL+C')

while 1:
    log = random.choice(logs)
    channel.basic_publish(exchange=exchange_name, routing_key='', body=log)
    print(f" [x] Published: {log}")
    time.sleep(1)
