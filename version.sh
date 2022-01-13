#
# Copyright (c) 2022 2bllw8
# SPDX-License-Identifier: Apache-2.0
#
# Update version script
# Usage:
#   ./version.sh "old.version" "new.version"

#!/usr/bin/env sh

function update_version {
  local OLD_VERSION="$2"
  local NEW_VERSION="$3"
  if [ -z $OLD_VERSION ] || [ -z $NEW_VERSION ]; then
    echo "Usage: $1 \$OLD_VERSION \$NEW_VERSION"
    exit 1
  else
    sed -i "s/version = '${OLD_VERSION}'/version = '${NEW_VERSION}'/" lib/build.gradle
    sed -i "s/${OLD_VERSION}/${NEW_VERSION}/" README.md
    git add README.md lib/build.gradle
    git commit -m "Version ${NEW_VERSION}"
    git tag "${NEW_VERSION}"
  fi
}

update_version $0 $1 $2
