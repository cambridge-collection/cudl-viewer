alter table collections
    add column metadescription TEXT NULL;

start transaction;

update collections set metadescription='Manuscripts and printed books from Cambridge University Library''s Chinese collections' where collectionid='chinese';
update collections set metadescription='Manuscripts and printed books from Cambridge University Library''s Christian collections' where collectionid='christian';
update collections set metadescription='The manuscripts of Charles Darwin, covering the formation of his theory of natural selection' where collectionid='darwin_mss';
update collections set metadescription='Charles Darwin''s correspondence with the botanist Joseph Dalton Hooker' where collectionid='darwinhooker';
update collections set metadescription='Selections from the collections of Downing College, Cambridge' where collectionid='downing';
update collections set metadescription='Fully digitised items from Cambridge University Library''s exhibitions programme' where collectionid='exhibitions';
update collections set metadescription='Photographs and journals of Thomas Fairbank, recording his experiences as an army doctor in the First World War' where collectionid='fairbank';
update collections set metadescription='The world''s largest and most important single collection of medieval Jewish manuscripts' where collectionid='genizah';
update collections set metadescription='Manuscripts and printed books from Cambridge University Library''s Hebrew collections' where collectionid='hebrew';
update collections set metadescription='Manuscripts and printed books from Cambridge University Library''s Islamic collections' where collectionid='islamic';
update collections set metadescription='Manuscripts and printed books from Cambridge University Library''s Japanese collections' where collectionid='japanese';
update collections set metadescription='Medieval Jewish manuscripts collected by the twin sisters Agnes Smith Lewis and Margaret Dunlop Gibson' where collectionid='lewisgibson';
update collections set metadescription='Treasures from a major exhibition to mark the 600th anniversary of Cambridge University Library' where collectionid='linesofthought';
update collections set metadescription='Papers of the Board of Longitude and related material from the National Maritime Museum, Greenwich' where collectionid='longitude';
update collections set metadescription='A collection of essays providing a rich contextual background to the Longitude material available on the Digital Library' where collectionid='longitudeessays';
update collections set metadescription='Maps in the collection of Cambridge University Library' where collectionid='maps';
update collections set metadescription='Selections from the largest private collection of Arabic papyri ' where collectionid='michaelides';
update collections set metadescription='Digital edition of the Mingana-Lewis Palimpsest, containing portions of two ancient Qurʼāns' where collectionid='minganalewis';
update collections set metadescription='Manuscripts and printed books from Cambridge University Library''s music collections' where collectionid='music';
update collections set metadescription='Photographs and notebooks belonging to the famous Cambridge sinologist Joseph Needham, from the collections of the Needham Research Institute' where collectionid='needham';
update collections set metadescription='The largest and most important collection of the mathematical and scientific works of Isaac Newton' where collectionid='newton';
update collections set metadescription='Manuscripts from the collections of the National Maritime Museum, Greenwich' where collectionid='nmm_mss';
update collections set metadescription='Printed books from the collections of the National Maritime Museum, Greenwich' where collectionid='nmm_print';
update collections set metadescription='Selections from the collections of Peterhouse, Cambridge' where collectionid='peterhouse';
update collections set metadescription='The working notebooks of the famous Cambridge botanist Oliver Rackham, from the archive of Corpus Christi College, Cambridge' where collectionid='rackham';
update collections set metadescription='Selections from the collection of the Royal Commonwealth Society, relating to the history of the Commonwealth and Britain''s former colonial territories' where collectionid='rcs';
update collections set metadescription='The complete run of the surviving papers of the Board of Longitude through the eighteenth century until its abolition in 1828' where collectionid='rgo14';
update collections set metadescription='The papers of Nevil Maskelyne, Astronomer Royal from 1765 to 1811' where collectionid='rgo4';
update collections set metadescription='The papers of John Pond, Astronomer Royal from 1811 to 1835' where collectionid='rgo5';
update collections set metadescription='Selections from the Royal Library, one of the most important donations to Cambridge University Library' where collectionid='royallibrary';
update collections set metadescription='A full catalogue of the Sanskrit manuscripts held by Cambridge Digital Library' where collectionid='sanskrit';
update collections set metadescription='The diaries, sketches and notebooks of the soldier-poet Siegfried Sassoon, including his own account of his experiences in the First World War' where collectionid='sassoon';
update collections set metadescription='Spanish Chapbooks from the collections of Cambridge University Library and the British Library' where collectionid='spanishchapbooks';
update collections set metadescription='Material relating to the British expeditions of 1874 to observe the rare astronomical phenomenon of the transit of Venus' where collectionid='tov';
update collections set metadescription='The great treasures from the collections of Cambridge University Library' where collectionid='treasures';
update collections set metadescription='Books, maps and manuscripts from an exhibition to mark the 200th anniversary of the Battle of Waterloo' where collectionid='waterloo';

commit;
