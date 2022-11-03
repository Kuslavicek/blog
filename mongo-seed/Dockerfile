FROM mongo

COPY init.json /init.json
CMD mongoimport --host mongodb --db test --collection authors --type json --file /init.json --jsonArray

