cp icons/icon.png icons/icon512.png
convert icons/icon512.png -resize 50% icons/icon256.png
convert icons/icon256.png -resize 50% icons/icon128.png
convert icons/icon128.png -resize 50% icons/icon64.png
convert icons/icon64.png -resize 75% icons/icon48.png
convert icons/icon64.png -resize 50% icons/icon32.png
convert icons/icon32.png -resize 50% icons/icon16.png