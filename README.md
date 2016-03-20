# ViewPagerBug
A project which reproduces a big in the viewpager causing ANR

When you have an inifite viewpager I.e. getcount is very big there is a very big lag when you call setCurrentItem with an index before the current index, this is caused by the following for loop within the `viewPager.populate(int)` method. When this code is reached `extraWidthRight >= rightWidthNeeded && pos > endPos` is `true` and `ii` is not null but ii.psition is smaller than the current ``mCurItem + 1` which results in the loop running from `mCurItem + 1` to N, which may be a very long loop 


``` java
for (int pos = mCurItem + 1; pos < N; pos++) {
  if (extraWidthRight >= rightWidthNeeded && pos > endPos) {
    if (ii == null) {
      break;
    }
    if (pos == ii.position && !ii.scrolling) {
      mItems.remove(itemIndex);
      mAdapter.destroyItem(this, pos, ii.object);
      if (DEBUG) {
        Log.i(TAG, "populate() - destroyItem() with pos: " + pos +
                    " view: " + ((View) ii.object));
      }
      ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
    }
  } else if (ii != null && pos == ii.position) {
    extraWidthRight += ii.widthFactor;
    itemIndex++;
    ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
  } else {
    ii = addNewItem(pos, itemIndex);
    itemIndex++;
    extraWidthRight += ii.widthFactor;
    ii = itemIndex < mItems.size() ? mItems.get(itemIndex) : null;
  }
}

```
