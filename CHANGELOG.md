### ~

### v0.6.2 (2018-02-03)
* adds option "verbose accessibility"; better support for TalkBack. 
* misc usability fixes (MainActivity, AlarmDialog); better support for TalkBack. 
* misc layout fixes (improved accessibility); better support for "large text".
* fixes bug "solstice/equinox dates not localized" (#146).
* adds "adaptive" launcher icon (used by api26+).
* updates translations (eo, pl) (#148, #149).
 
### v0.6.1 (2018-01-23)
* adds translations to Catalan (ca) and Spanish (es-ES) (contributed by Raulvo) (#141).
* fixes bug; expected "11h 55s", actual "11h55s" (#b61d942).
* enhances the calculator selector used by widget configuration (now shows descriptive text).
* misc accessibility fixes (labelFor, dropDownVerticalOffset).
* adds web links in the About Dialog to the changelog and version commit.  
* updates translations (eo, pl) (#137).

### v0.6.0 (2017-12-27)
* adds solstice/equinox tracking to app (#13).
* adds solstice/equinox widget (#13).
* adds option to show seconds in rise/set times displayed by widgets.
* adds option to show seconds in rise/set/delta times displayed by the app.

### v0.5.2 (2017-12-21)
* fixes "tomorrow will be" comparison; erroneously reported 1m (when actually 0s) for non-simple sources (1m is correct for sunrisesunsetlib and time4a-simple).
* misc refactoring to prevent memory leaks (LightMapTask, TimeZonesLoadTask).
* fixes flippable widget randomly displays 24hr time (#129).
* fixes table switch animation fails to play (#125).
* fixes automatic keyboard popup (WidgetConfigActivity, ThemeConfigActivity); prevent the keyboard from taking focus on activity start. 
* fixes stale/duplicate items in theme selector.
* fixes theme preview icons (now shown) (api22+).
* fixes theme icons don't use theme colors (api22+) (#120).
* fixes crash when adding widgets (api22+) (#126).
* updates dependency (Time4A 3.38-2017c).
* updates translations (eo, pl) (#123, #124).

### v0.5.1 (2017-12-03)
* changes default data source to time4a-noaa (fallback remains sunrisesunsetlib).
* fixes 2x1 widget to show seconds (e.g. "Tomorrow will be 1m 4s shorter").
* restricts auto-backup to app settings and themes (now excludes widget settings and sqlite db).
* adds collapsed UI state to ColorChooser; expanded by clicking label. 
* adds to theme config activity: sunrise, sunset, and noon icon colors (fill, stroke, stroke width).
* fixes theme previews to display sunrise/sunset times (as configured by app) instead of static text.
* fixes widget sunrise, sunset, and noon icons to use theme colors.
* fixes widget titles to marquee over a single line when too long.
* fixes widgets to allow for vertical resize.
* lists 2x1 widget (previously only accessible by resizing 1x1 widget).
* fixes 2x1 layout for api versions <= 15 (previously inaccessible).
* updates dependency (Time4A 3.37-2017c).
* updates translations (eo, pl) (#115, #116).

### v0.5.0 (2017-11-18)
* auto-backup reenabled.
* adds data source; Time4A (time4a-simple, time4a-noaa, time4a-cc, time4a-time4j) (contributions by MenoData) (#103).
* adds support for custom themes; theme editor activity (add / edit), theme selector activity (copy / delete / export) (#7).
* adds widget option "show noon" (#102); adds noon field to 1x3 widget, adds noon to flippable widget.
* adds widget option "show comparison" (show/hide comparison field on 1x3 widgets).
* adds option to show data source label in app UI / misc data source related UI enhancements.
* adds translation to Hungarian (hu) (contributed by Erci) (#106).
* updates translations (eo, pl) (#110).

### v0.4.1 (2017-10-31)
* fixes data source setting not honored (#104).

### v0.4.0 (2017-06-12)
* adds translation to French (contributed by Jej) (#92).
* adds time format option (12hr / 24hr time) (#22).
* adds daylight savings time warning (#90).
* automatic backups now disabled.
* fixes app layout; times unreadable when using large font setting (related to #43).
* fixes lightmap rendering (now optimized to do work off the UI thread).
* updates translations (eo, pl) (#93, #94, #96, #97).

### v0.3.1 (2017-04-15)
* misc UI tweaks, styles, and strings.
* fixes widget preview images.
* fixes ui bug "table switches unintentionally" (#20).
* fixes "allow resize" option (disabled for api 16 and under).
* updates translations (eo, pl) (#86, #87).
* adds Junit and Espresso UI testing to the project.

### v0.3.0 (2017-02-23)
* adds data source; ca.rmen.sunrisesunset.
* adds solar time mode; local mean time, and apparent solar time (#66).
* adds "light map" horizontal stacked bar chart.
* adds "ui warnings" (snackbar alerts for unusual date or time zone configurations) (#54).
* adds time zone selector enhancements (color coding, sort by ID or UTC offset).
* adds widget layout option "allow resize" (disable widget resizing).
* adds widget title substitution; %s is for data source.
* fixes app crash (latitude edge case) (#74).
* fixes app crash ("set date" api10) (#75).
* fixes time zone selector loading (now loads asynchronously).
* fixes widget layout update bug / widget resizes itself after update (#77).
* fixes widget update behavior (now updates at midnight) (#77).
* updates translations (eo, pl) (#69, #72, #73, #79, #80, #83, #84).

### v0.2.3 (2016-11-07)
* fixes alarm set incorrectly w/ user-defined timezones (#64).
* fixes solar noon sometimes incorrect for user-defined timezones (#65).
* fixes ui bug "dialog state lost on orientation change" (#63).
* fixes ui bug where TimeDateDialog settings were not immediately applied.

### v0.2.2 (2016-09-22)
* use network location provider if available (#49).
* fixes gps prefs not honored (#50).
* fixes app crash on SettingsActivity (api14, api15) (#55).
* fixes app crash on ExportPlaces (api18) (adds permission EXTERNAL_STORAGE) (#67).
* fixes misc SettingsActivity bugs (api10, api15) (#57, #58, #59, #60).
* updates translations (eo, pl) (#51).

### v0.2.1 (2016-08-30)
* fixes unreadable app layouts when using non-english locales (#43).
* fixes missing actionbar overflow icons.
* fixes lat/lon input; touch dialog fields to begin editing (#37).
* updates translations (eo, pl) (#46, #47).

### v0.2.0 (2016-08-13)
* adds translation to Polish (pl) and Esperanto (eo) (contributed by Verdulo) (#24, #26, #31, #35, #36).
* adds option to override the locale from within the app (#23).
* adds default location provided by the locale (#33).
* fixes lat/lon decimal separator bug (#29); fails to set the location when locale uses "," as the decimal separator.
* fixes timezone bug (#34); timezone ignored when using 24hr time.
* fixes ui bug "note doesn't adapt user defined dates" (#19).
* fixes ui bug "table switches unintentionally" (#20).

### v0.1.1 (2016-07-21)
* adds support for localization.
* adds translation to German (de) (contributed by Henrik "HerHde" H�ttemann) (#16).
* fixes app crash when using Show map without an installed map application.
* fixes app crash when sunrise or sunset does not occur for a given date/location.
* fixes "no data source" bug (#14); default value not properly displayed by settings -> general -> dataSource.
* fixes lat/lon precision; was unlimited but now rounded to 5 places (meter precision).
* fixes lat/lon input validation.

### v0.1.0 (2016-07-04)
* adds app activity that displays times for a given location and date
* adds widgetlist activity for reconfiguring home screen widgets
* fixes lat/lon input field bug

### v0.0.2 (2016-03-11)
* adds flippable widget
* adds ontap actions (reconfigure, launch activity)
* adds get gps fix (user defined location)

### v0.0.1 (2015-02-16)
* basic widget
