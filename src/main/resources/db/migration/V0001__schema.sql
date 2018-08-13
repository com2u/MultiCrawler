
    create table public.domain (
        id int8 not null,
        domain varchar(255) not null,
        subdomain varchar(255) not null,
        ip varchar(255) not null,
        created_date timestamp not null,
        primary key (id)
    );

    create table public.page (
        id int8 not null,
        domain_id int8 not null,
        link varchar(2000) not null,
        url varchar(2000) not null,
        title varchar(255) null,
        language_tag varchar(255) null,
        line_count int null default 0,
        number_count int8 null default 0,
        letter_count int8 null default 0,
        word_count int8 null default 0,
        sentence_count int null default 0,
        html_heading_count int null default 0,
        html_paragraph_count int null default 0,
        html_div_count int null default 0,
        internal_link_count int null default 0,
        external_link_count int null default 0,
        list_of_links text null,
        created_date timestamp not null,
        visited_date timestamp null,
        primary key (id)
    );

    alter table public.domain
        add constraint uniqueSubdomain  unique (subdomain);

    alter table public.page
        add constraint uniqueLink  unique (link);

    alter table public.page
        add constraint uniqueUrl  unique (url);

    alter table public.page
        add constraint FK_cgfgfs7fk42h7ck71lrs42sou
        foreign key (domain_id)
        references public.domain;

    create sequence public.seq_domain_id;

    create sequence public.seq_page_id;


