# This scripts clears all documents and collections on the firestore
import firebase_admin
from firebase_admin import credentials, firestore

def delete_collection(collection_ref, batch_size):
    if batch_size == 0:
        return


    documents = collection_ref.list_documents(page_size=batch_size)
    deleted = 0


    for doc in documents:
        print(f"Deleting document {doc.id} => {doc.get().to_dict()}")
        doc.delete()
        deleted += 1


    if deleted >= batch_size:
        return delete_collection(collection_ref, batch_size)


cred = credentials.Certificate("../data/key/comic-app-b344c-firebase-adminsdk-ot4l1-e17e2b10be.json")
app = firebase_admin.initialize_app(cred)

db = firestore.client()

for collection in db.collections():
    delete_collection(collection, 500)
