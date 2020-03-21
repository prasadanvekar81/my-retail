import json
from pymongo import MongoClient


client = MongoClient('localhost', 27017)
db = client['my_retail']
collection_product = db['product']

with open('products.json') as f:
    file_data = json.load(f)

collection_product.insert(file_data)
print "Import Succeeded"
client.close()