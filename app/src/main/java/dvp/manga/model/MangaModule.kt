package dvp.manga.model

import io.realm.annotations.RealmModule

/**
 * @author dvphu on 1/23/2018.
 */

@RealmModule(classes = [(Manga::class), (Chapter::class), (Page::class)])
class MangaModule