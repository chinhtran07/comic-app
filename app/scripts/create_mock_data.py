# This scripts dumps data into json files in ../data/*.json
import bcrypt
import json
import uuid
from datetime import datetime
from zoneinfo import ZoneInfo

def hash_password(password):
    salt = bcrypt.gensalt()
    hashed_password = bcrypt.hashpw(password.encode(), salt)
    return hashed_password

# User Collection
users = [
    {
        "id": str(str(uuid.uuid4())),
        "username": "admin",
        "password": str(hash_password("123456")),
        "email": "admin@gmail.com",
        "user_role": "ADMIN"
    },
    {
        "id": str(str(uuid.uuid4())),
        "username": "binh",
        "password": str(hash_password("123456")),
        "email": "binh@gmail.com",
        "user_role": "READER"
    },
    {
        "id": str(str(uuid.uuid4())),
        "username": "an",
        "password": str(hash_password("123456")),
        "email": "an@gmail.com",
        "user_role": "READER"
    }
]

# Category Collection
categories = [
    {
        "id": str(uuid.uuid4()),
        "name": "Thiếu nhi"
    },
    {
        "id": str(uuid.uuid4()),
        "name": "Tiểu thuyết"
    }
]

authors = [
    {
        "id": str(uuid.uuid4()),
        "name": "Hiroshi Fujimoto"
    },
    {
        "id": str(uuid.uuid4()),
        "name": "Motou Abiko"
    },
    {
        "id": str(uuid.uuid4()),
        "name": "Lewis Carroll"
    }
]


# Title Collection
titles = [
    {
        "id": str(uuid.uuid4()),
        "name": "Doraemon Truyện Dài",
        "pub_status": "COMPLETED",
        "views": 200,
        "created_date": datetime(year=2024, month=7, day=2, hour=8, minute=30, second=12, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "format": "COMIC",
        "cover": "cover_path",
        "category_id": categories[0]["id"]
    },
    {
        "id": str(uuid.uuid4()),
        "name": "Alice ở xứ sở thần tiên (ENGLISH)",
        "pub_status": "COMPLETED",
        "views": 23,
        "created_date": datetime(year=2020, month=12, day=27, hour=17, minute=30, second=12, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "format": "NOVEL",
        "cover": "cover_path",
        "category_id": categories[1]["id"]
    }
]

titles_authors = [
    {
        "id": str(uuid.uuid4()),
        "title_id": titles[0]["id"],
        "author_id": authors[0]["id"]
    },
    {
        "id": str(uuid.uuid4()),
        "title_id": titles[0]["id"],
        "author_id": authors[1]["id"]
    },
    {
        "id": str(uuid.uuid4()),
        "title_id": titles[1]["id"],
        "author_id": authors[2]["id"]
    }
]

# Chapter Collection
chapters = [
    {
        "id": str(uuid.uuid4()),
        "chapter_number": 1,
        "uploaded_date": datetime(year=2024, month=7, day=2, hour=8, minute=35, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "title_id": titles[0]["id"],
        "content": "content_path"
    },
    {
        "id": str(uuid.uuid4()),
        "chapter_number": 2,
        "uploaded_date": datetime(year=2024, month=7, day=2, hour=8, minute=35, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "title_id": titles[0]["id"],
        "content": "content_path"
    },
    {
        "id": str(uuid.uuid4()),
        "chapter_number": 3,
        "uploaded_date": datetime(year=2024, month=7, day=2, hour=8, minute=35, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "title_id": titles[0]["id"],
        "content": "content_path"
    },
    {
        "id": str(uuid.uuid4()),
        "chapter_number": 1,
        "uploaded_date": datetime(year=2020, month=12, day=27, hour=18, minute=0, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "title_id": titles[1]["id"],
        "content": "content_path"
    },
    {
        "id": str(uuid.uuid4()),
        "chapter_number": 2,
        "uploaded_date": datetime(year=2020, month=12, day=27, hour=18, minute=0, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "title_id": titles[1]["id"],
        "content": "content_path"
    },
    {
        "id": str(uuid.uuid4()),
        "chapter_number": 3,
        "uploaded_date": datetime(year=2020, month=12, day=27, hour=18, minute=0, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
        "title_id": titles[1]["id"],
        "content": "content_path"
    }
]


# Page Collection
pages = []
for chapters_idx in range(len(chapters)):
    for page_idx in range(3):
        pages.append({
            "id": str(uuid.uuid4()),
            "data_path": "path_string " + str(page_idx),
            "chapter_id": chapters[chapters_idx]["id"]
        })

# ReadingHistory Collection
reading_histories = [
    {
        "id": str(uuid.uuid4()),
        "user_id": users[1]["id"],
        "title_id": titles[0]["id"],
        "latest_read": datetime(year=2024, month=7, day=3, hour=18, minute=0, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
    },
    {
        "id": str(uuid.uuid4()),
        "user_id": users[1]["id"],
        "title_id": titles[1]["id"],
        "latest_read": datetime(year=2024, month=6, day=29, hour=0, minute=20, second=0, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
    },
    {
        "id": str(uuid.uuid4()),
        "user_id": users[2]["id"],
        "title_id": titles[0]["id"],
        "latest_read": datetime(year=2023, month=1, day=20, hour=19, minute=11, second=59, tzinfo=ZoneInfo("Asia/Ho_Chi_Minh")).isoformat(),
    }
]

replied_cmt_id = str(uuid.uuid4())
comments = [
    {
        "id": replied_cmt_id,
        "content": "Truyện hay, bổ ích cho con trẻ",
        "base_comment_id": None,
        "user_id": users[1]["id"],
        "title_id": titles[0]["id"],
    },
    {
        "id": str(uuid.uuid4()),
        "content": "Truyện nhân văn, tính nghệ thuật pha trộn với kì ảo làm nổi bật lên những nhân vật trong truyện.",
        "base_comment_id": None,
        "user_id": users[1]["id"],
        "title_id": titles[1]["id"],
    },
    {
        "id": str(uuid.uuid4()),
        "content": "Comment hay",
        "base_comment_id": replied_cmt_id,
        "user_id": users[2]["id"],
        "title_id": titles[0]["id"],
    }
]

filedata = [
    {
        "name": "users",
        "data": users
    },
    {
        "name": "categories",
        "data": categories
    },
    {
        "name": "titles",
        "data": titles
    },
    {
        "name": "chapters",
        "data": chapters
    },
    {
        "name": "pages",
        "data": pages
    },
    {
        "name": "reading_histories",
        "data": reading_histories
    },
    {
        "name": "comments",
        "data": comments
    },
    {
        "name": "authors",
        "data": authors
    },
    {
        "name": "titles_authors",
        "data": titles_authors
    },
]

for idx in range(len(filedata)):
    with open(f"../data/{filedata[idx]["name"]}.json", "w", encoding="utf-8") as write_file:
        json.dump(filedata[idx]["data"], write_file, indent=4, ensure_ascii=False)

