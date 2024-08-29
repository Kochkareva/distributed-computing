import pika

connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
channel = connection.channel()
exchange_name = 'logs'
queue_name = 'fast-queue'

channel.queue_declare(queue=queue_name)
channel.queue_bind(exchange=exchange_name, queue=queue_name)


def callback(ch, method, properties, body):
    task = body.decode()
    print(f" [x] Received : {task}")
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
