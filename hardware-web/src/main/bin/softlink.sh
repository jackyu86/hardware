#!/bin/sh
rm  -rf /export/data/sccd/WEB-INF/publish
echo "remove WEB-INF/publish successfully!"
ln -sfT /export/data/test/publish /export/data/sccd/WEB-INF/publish
echo "publish softlink successfully!"