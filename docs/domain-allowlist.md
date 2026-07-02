# Domain Allowlist Planning

The domain allowlist is intended for restricted mode: a device has no normal internet access, but can still open a small set of useful websites.

The goal is to allow necessary services without opening video, games, social feeds, or entertainment platforms.

## Product Rules

- Keep the domain allowlist disabled by default until the user enables it.
- Ship suggested presets, not an overly broad mandatory list.
- Let parents edit every preset.
- Prefer narrow domains and subdomains over broad parent domains.
- Explain that domain allowlisting is imperfect because of HTTPS, CDN hosting, mobile apps, DoH, shared domains, and browser cache.
- Do not include movie, game, social network, short-video, streaming, or app-store domains in the default allowlist.

## Suggested Presets

### Government And Identity

Useful for documents, public services, and identity flows.

```text
gosuslugi.ru
esia.gosuslugi.ru
lk.gosuslugi.ru
pos.gosuslugi.ru
mos.ru
my.mos.ru
school.mos.ru
```

### School And Education

These should be editable because school platforms vary by region.

```text
dnevnik.ru
login.dnevnik.ru
school.mos.ru
edu.gosuslugi.ru
uchi.ru
resh.edu.ru
```

### Search And Basic Tools

Use carefully. Search engines can lead to entertainment content even when the top-level domain looks harmless.

```text
ya.ru
google.com
translate.google.com
maps.google.com
2gis.ru
```

Avoid adding broad Yandex domains by default:

```text
yandex.ru
video.yandex.ru
kinopoisk.ru
music.yandex.ru
games.yandex.ru
dzen.ru
```

If Yandex search is needed, prefer `ya.ru` first and let parents explicitly add broader Yandex domains later.

### Communication And Safety

Optional. These can be useful, but they may also become a way around restrictions.

```text
mail.google.com
mail.yandex.ru
web.whatsapp.com
telegram.org
```

Do not enable this preset by default.

## Default Recommendation

Start with no domains enabled automatically.

Offer presets in the UI:

- Government;
- School;
- Search and maps;
- Communication;
- Custom domains.

The safest first-run suggestion is:

```text
gosuslugi.ru
esia.gosuslugi.ru
mos.ru
school.mos.ru
dnevnik.ru
ya.ru
2gis.ru
```

Parents should review and confirm the list before it becomes active.
