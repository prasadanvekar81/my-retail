FROM mongo

COPY init.json /init.json
CMD mongoimport --host mongodb --db my_retail --collection product --type json --file /init.json --jsonArray