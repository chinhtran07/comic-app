# This script push the database into the firebase firestore using data in ../data/*.json
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import json


def batch_execute(item_list, db_ref, collection, item_id='id', ref_field=''):
    try:
        print(f"Creating collection {collection}")
        batch = db_ref.batch()
        for item in item_list:
            print(f"Creating document {item[item_id]}")
            doc_ref = db.collection(collection).document(item[item_id])
            del item[item_id]
            batch.set(doc_ref, item)
        batch.commit()
    except Exception as e:
        print(f"Error processing batch write: {e.with_traceback()}")

cred = credentials.Certificate("../data/key/comic-app-b344c-firebase-adminsdk-ot4l1-e17e2b10be.json")
app = firebase_admin.initialize_app(cred)


db = firestore.client()

filedata = [
    {
        "name": "users",
    },
    {
        "name": "genres",
    },
    {
        "name": "titles",
    },
    {
        "name": "chapters",
    },
    {
        "name": "pages",
    },
    {
        "name": "reading_histories",
    },
    {
        "name": "comments",
    },
    {
        "name": "authors"
    },
    {
        "name": "titles_authors"
    }
]


for idx in range(len(filedata)):
    with open(f"../data/{filedata[idx]["name"]}.json", encoding="utf-8") as read_file:
        filedata[idx]["data"] = json.load(read_file)
        read_file.close()

for idx in range(len(filedata)):
    batch_execute(filedata[idx]["data"], db, filedata[idx]["name"])
