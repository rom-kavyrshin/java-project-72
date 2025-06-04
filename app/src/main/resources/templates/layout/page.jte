@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.base.BasePage
@import gg.jte.Content
@param Content content
@param BasePage page = null

<html lang="en">
<head>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-4Q6Gf2aSP4eDXB8Miphtr37CMZZQ5oXLH2yaXMJ2w8e2ZtHTl7GptT4jmndRuHDT" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.6/dist/js/bootstrap.bundle.min.js" integrity="sha384-j1CDi7MgGQ12Z7Qab0qlWQ/Qqz24Gc6BM0thvEMVjHnfYGF0rmFCozFSxQBxwHKO" crossorigin="anonymous"></script>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>Анализатор страниц</title>
</head>
<body>
<header>
    <nav class="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="${NamedRoutes.rootPath()}">Анализатор страниц</a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarColor02"
                    aria-controls="navbarColor02" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarColor02">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item"><a class="nav-link" href="${NamedRoutes.rootPath()}">Главная</a></li>
                    <li class="nav-item"><a class="nav-link" href="${NamedRoutes.rootPath()}">Сайты</a></li>
                </ul>
            </div>
        </div>
    </nav>
</header>
@if(page != null && page.getFlashMessage() != null)
    <div class="alert alert-${page.getFlashMessage().isSuccess() ? "success" : "danger"}" role="alert">${page.getFlashMessage().getMessage()}</div>
@endif
<div>
    ${content}
</div>
<footer>
    <h3><a href="https://github.com/rom-kavyrshin">My github</a></h3>
</footer>
</body>
</html>
