package dvp.manga

import dvp.manga.model.Chapter
import dvp.manga.model.Manga
import dvp.manga.model.MangaInfo
import dvp.manga.model.Page

/**
 * @author Zero on 1/18/2018.
 */

abstract class GetDataCallback {

    open fun onGetMangaFinished(mangas: List<Manga>) {

    }

    open fun onGetChapterFinished(chapters: List<Chapter>) {

    }

    open fun onGetMangaPageFinished(pages: List<Page>) {

    }

    open fun onGetMangaInfoFinished(info: MangaInfo) {

    }

    open fun onSearchMangaFinish(mangas: List<Manga>?) {

    }
}