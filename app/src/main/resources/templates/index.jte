@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.root.RootPage
@param RootPage page
@template.layout.page(
page = page,
content = @`
    <main>
        <div class="container-fluid bg-primary p-5">
            <div class="row">
                <div class="col-md-10 col-lg-8 mx-auto text-white">
                    <h1 class="display-3 mb-0">Анализатор страниц</h1>
                    <p class="lead">Бесплатно проверяйте сайты на SEO пригодность</p>
                    <form action="${NamedRoutes.urlsPath()}" method="post" class="rss-form text-body">
                        <div class="row">
                            <div class="col">
                                <div class="form-floating">
                                    <input id="url-input" autofocus="" type="text" required="" name="url" aria-label="url" class="form-control w-100" placeholder="ссылка" autocomplete="off">
                                    <label for="url-input">Ссылка</label>
                                </div>
                            </div>
                            <div class="col-auto">
                                <button type="submit" class="h-100 btn btn-lg btn-light px-sm-5">Проверить</button>
                            </div>
                        </div>
                    </form>
                    <p class="mt-2 mb-0">Пример: https://www.example.com</p>
                </div>
            </div>
        </div>
    </main>
`)