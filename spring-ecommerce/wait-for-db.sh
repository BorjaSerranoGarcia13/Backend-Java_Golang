#!/bin/sh

set -e

host="$1"
shift
cmd="$@"

until mysql -h"$host" -P3306 -uroot -proot -e 'SELECT 1'; do
  >&2 echo "Database is unavailable - sleeping"
  sleep 1
done

>&2 echo "Database is up - executing command"
exec $cmd