#!/bin/bash

read -p "version : " version

cd ../toast.gamebase.android.sample

git remote add sample ../gamebase.client.android.sample
git remote update
git merge sample/develop -Xtheirs --allow-unrelated-histories --squash --no-edit
git commit -m "Update Gamebase Android SDK $version"
git rm deploy.sh
git add .
git commit --amend --no-edit --allow-empty
git push -u origin main
git remote remove sample

curl --location --request POST 'https://api.github.com/repos/nhn/toast.gamebase.android.sample/releases' \
--header 'Accept: application/vnd.github.v3+json' \
--header 'Authorization: token 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' \
--header 'Content-Type: application/json' \
--data-raw '{
    "target_commitish": "main",
    "tag_name" : "'$version'",
    "name" : "'$version'",
    "body" : "Release Note : https://docs.toast.com/ko/Game/Gamebase/ko/release-notes-android/"
}'
