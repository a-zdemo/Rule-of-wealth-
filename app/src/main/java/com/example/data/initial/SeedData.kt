package com.example.data.initial

import com.example.data.model.Achievement
import com.example.data.model.CardCategory
import com.example.data.model.CardDefinition
import com.example.data.model.CardRarity
import com.example.data.model.MarketListing
import com.example.data.model.Mission
import com.example.data.model.MissionCategory
import com.example.data.model.PlayerTier
import com.example.data.model.Recipe
import com.example.data.model.TradeOffer
import com.example.data.model.TradeStatus
import com.example.data.model.ListingStatus

object SeedData {

    val INITIAL_CARDS = listOf(
        CardDefinition(
            id = "card_saver",
            cardKey = "the_saver",
            name = "The Saver",
            category = CardCategory.STARTER,
            rarity = CardRarity.COMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Capital preservation through disciplined cashflow retention.",
            conceptExplanation = "Saving is retaining surplus income before spending. It provides liquidity, prevents emergency debt, and forms the foundational seed capital needed to seize opportunistic investments.",
            practicalExample = "Setting aside 20% of monthly earnings into a liquid high-yield reserve before budgeting for living expenses.",
            basePrice = 80,
            quizQuestion = "Why is saving the essential first step before aggressive investing?",
            quizOptionsJson = "It guarantees high immediate yield|It provides emergency liquidity and prevents forced liquidation of assets|It eliminates all market volatility|It replaces the need to generate income",
            quizCorrectIndex = 1,
            quizExplanation = "Savings protect you from being forced to sell assets at a loss during downturns and provide initial deployment capital."
        ),
        CardDefinition(
            id = "card_trader",
            cardKey = "the_trader",
            name = "The Trader",
            category = CardCategory.STARTER,
            rarity = CardRarity.COMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Exploiting price discrepancies across markets or time.",
            conceptExplanation = "Arbitrage and market-making create value by moving assets from where they are undervalued to where they are in highest demand, capturing the spread.",
            practicalExample = "Purchasing clearance inventory locally for \$10 and reselling online where market clearing price is \$35.",
            basePrice = 90,
            quizQuestion = "What creates profit in a classic trading or arbitrage model?",
            quizOptionsJson = "Pure luck and market timing|The spread between purchase cost and buyer willingness-to-pay|Holding an asset forever without selling|Paying high transaction fees",
            quizCorrectIndex = 1,
            quizExplanation = "Trading profit comes from capturing value differences (the spread) between supply access and buyer demand."
        ),
        CardDefinition(
            id = "card_product_idea",
            cardKey = "product_idea",
            name = "Product Idea",
            category = CardCategory.BUSINESS_OPPORTUNITY,
            rarity = CardRarity.COMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Identifying an acute, underserved customer pain point.",
            conceptExplanation = "A product idea is a hypothesized solution to a recurring friction. Ideas carry zero intrinsic value until validated through customer willingness to pay.",
            practicalExample = "Recognizing that freelance consultants waste 8 hours weekly tracking billable hours and drafting a streamlined automated tracker.",
            basePrice = 100,
            quizQuestion = "What gives a product idea its real-world economic value?",
            quizOptionsJson = "Having a patent registered before talking to customers|Validation through customer willingness to pay and execution|Telling friends on social media|Keeping it completely secret",
            quizCorrectIndex = 1,
            quizExplanation = "An idea only gains value when customers confirm with their wallets that it solves a genuine painful problem."
        ),
        CardDefinition(
            id = "card_digital_distribution",
            cardKey = "digital_distribution",
            name = "Digital Distribution",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.COMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Near-zero marginal cost delivery across global networks.",
            conceptExplanation = "Digital distribution allows goods (software, ebooks, courses, templates) to be replicated and delivered infinitely without physical shipping, warehousing, or manufacturing costs.",
            practicalExample = "Hosting a video tutorial library on a CDN, delivering to 100,000 customers with identical overhead as serving 10 customers.",
            basePrice = 110,
            quizQuestion = "What is the primary economic advantage of digital distribution?",
            quizOptionsJson = "High manufacturing expenses|Near-zero marginal cost per additional unit delivered|Requirement for local physical storefronts|Complex international freight customs",
            quizCorrectIndex = 1,
            quizExplanation = "Near-zero marginal cost allows gross profit margins to exceed 85-95%."
        ),
        CardDefinition(
            id = "card_content_creation",
            cardKey = "content_creation",
            name = "Content Creation",
            category = CardCategory.SKILL,
            rarity = CardRarity.COMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Producing high-value educational or engaging media.",
            conceptExplanation = "Content creation transforms domain expertise into digital media (articles, videos, newsletters) that builds organic trust and captures consumer attention.",
            practicalExample = "Publishing weekly in-depth tutorials analyzing software architecture best practices on YouTube.",
            basePrice = 100,
            quizQuestion = "How does free content generate leverage for business?",
            quizOptionsJson = "It instantly depletes cashflow|It builds authentic trust, domain authority, and organic inbound attention|It legally forces viewers to purchase|It avoids the need for an audience",
            quizCorrectIndex = 1,
            quizExplanation = "High-quality content acts as an automated 24/7 trust engine for prospective buyers."
        ),
        CardDefinition(
            id = "card_audience_building",
            cardKey = "audience_building",
            name = "Audience Building",
            category = CardCategory.SKILL,
            rarity = CardRarity.UNCOMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Aggregating and retaining permission-based attention.",
            conceptExplanation = "An audience of opt-in subscribers or followers represents owned distribution. Instead of paying ad platforms repeatedly, you communicate with interested buyers directly.",
            practicalExample = "Growing an email newsletter to 25,000 active decision-makers with a consistent 45% open rate.",
            basePrice = 150,
            quizQuestion = "Why is an opt-in email list more valuable than algorithmic social followers?",
            quizOptionsJson = "Emails cost thousands per send|You own direct permission access, unaffected by platform algorithm changes|Emails cannot be read on mobile devices|Social followers are always paying customers",
            quizCorrectIndex = 1,
            quizExplanation = "Owned distribution channels protect your business from sudden algorithm de-ranking or platform policy changes."
        ),
        CardDefinition(
            id = "card_product_sourcing",
            cardKey = "product_sourcing",
            name = "Product Sourcing",
            category = CardCategory.SKILL,
            rarity = CardRarity.UNCOMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Negotiating reliable manufacturing and supply chains.",
            conceptExplanation = "Sourcing secures cost-effective, high-quality inventory at wholesale rates. Profit in physical commerce is often locked in at the point of purchase, not just sale.",
            practicalExample = "Negotiating directly with tier-1 component manufacturers for bulk unit discounts and net-30 terms.",
            basePrice = 140,
            quizQuestion = "In commerce, why is it said that 'profit is made when you buy'?",
            quizOptionsJson = "Because customers will pay any price you ask|Securing favorable unit costs and terms guarantees margin safety regardless of market fluctuations|Because buying inventory automatically generates sales|Suppliers always refund unsold stock",
            quizCorrectIndex = 1,
            quizExplanation = "Favorable purchase prices protect profit margins even if market retail prices soften."
        ),
        CardDefinition(
            id = "card_sales_strategy",
            cardKey = "sales_strategy",
            name = "Sales Strategy",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.UNCOMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Structured conversion of interest into closed revenue.",
            conceptExplanation = "Sales is the disciplined process of qualification, value demonstration, objection handling, and closing. Great products fail without structured sales pipelines.",
            practicalExample = "Implementing a 5-step enterprise discovery call framework that lifts qualified lead close rates from 12% to 28%.",
            basePrice = 160,
            quizQuestion = "What is the primary function of a sales discovery process?",
            quizOptionsJson = "To talk continuously about all product features|To diagnose customer pain points and qualify budget and authority|To lower price immediately to get a signature|To send generic automated emails",
            quizCorrectIndex = 1,
            quizExplanation = "Effective sales is diagnostic: understanding customer pain and aligning your solution with their quantified financial value."
        ),
        CardDefinition(
            id = "card_skill_development",
            cardKey = "skill_development",
            name = "Skill Development",
            category = CardCategory.SKILL,
            rarity = CardRarity.COMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Investing in rare, high-leverage technical or managerial capabilities.",
            conceptExplanation = "Acquiring specialized, difficult-to-replicate skills increases your personal hourly leverage and provides the foundation for consulting or technical ventures.",
            practicalExample = "Mastering cloud backend infrastructure and database optimization, moving from $30/hr to $150/hr enterprise retainers.",
            basePrice = 100,
            quizQuestion = "What type of skills provide the highest economic return?",
            quizOptionsJson = "Easily automated repetitive tasks|Commodity skills with millions of identical competitors|Rare and valuable skills that sit at the intersection of technical depth and commercial value|Skills that require no deliberate practice",
            quizCorrectIndex = 2,
            quizExplanation = "Skills that are both rare and directly tied to revenue or cost-reduction command premium market rates."
        ),
        CardDefinition(
            id = "card_customer_acquisition",
            cardKey = "customer_acquisition",
            name = "Customer Acquisition",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.UNCOMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Predictable systems for acquiring paying customers at scale.",
            conceptExplanation = "Understanding Customer Acquisition Cost (CAC) relative to Customer Lifetime Value (LTV) determines whether a business can sustainably scale.",
            practicalExample = "Building an organic referral engine and paid search campaign yielding an LTV:CAC ratio of 4.2 to 1.",
            basePrice = 170,
            quizQuestion = "What is considered a healthy LTV to CAC ratio for a sustainable business?",
            quizOptionsJson = "0.5 : 1 (Spending twice what customers pay)|At least 3 : 1 (Customer lifetime value is at least 3x the acquisition cost)|1 : 1 (Breaking even with zero margin)|LTV doesn't matter as long as revenue grows",
            quizCorrectIndex = 1,
            quizExplanation = "An LTV:CAC ratio of 3:1 or higher provides sufficient margin to cover operating expenses, churn, and reinvestment."
        ),
        CardDefinition(
            id = "card_market_research",
            cardKey = "market_research",
            name = "Market Research",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.COMMON,
            tierRequirement = PlayerTier.STARTER,
            shortDescription = "Gathering objective data on competitor flaws and unmet demand.",
            conceptExplanation = "Systematic analysis of competitor pricing, negative customer reviews, and keyword trends prevents costly build efforts on unwanted products.",
            practicalExample = "Analyzing 500 one-star reviews of market leader software to isolate the top 3 unaddressed customer frustrations.",
            basePrice = 110,
            quizQuestion = "What is the best source of insights for market research?",
            quizOptionsJson = "Your own unvalidated assumptions|Competitor customer complaints, spending patterns, and search intent|Asking friends if they think an idea sounds cool|Random social media polls",
            quizCorrectIndex = 1,
            quizExplanation = "Complaints from people already spending money in a market reveal exactly what paying customers want improved."
        ),
        CardDefinition(
            id = "card_digital_product",
            cardKey = "digital_product",
            name = "Digital Product",
            category = CardCategory.ADVANCED_OPPORTUNITY,
            rarity = CardRarity.RARE,
            tierRequirement = PlayerTier.EMERGING,
            shortDescription = "Packaged digital intellectual property sold globally.",
            conceptExplanation = "Created by combining a validated Product Idea with Digital Distribution. Once created, each sale generates pure profit with zero shipping or physical inventory holding cost.",
            practicalExample = "A comprehensive financial modeling spreadsheet and companion video guide selling 500 units/month at $99.",
            basePrice = 280,
            quizQuestion = "What makes Digital Products uniquely attractive to solopreneurs?",
            quizOptionsJson = "They require massive physical warehouses|They generate high gross margins and can be automated to sell 24/7 globally|They are legally immune to taxes|They require zero customer support",
            quizCorrectIndex = 1,
            quizExplanation = "High gross margins (90%+) and zero inventory logistics allow small teams to generate outsized cashflow."
        ),
        CardDefinition(
            id = "card_service_business",
            cardKey = "service_business",
            name = "Service Business",
            category = CardCategory.ADVANCED_OPPORTUNITY,
            rarity = CardRarity.RARE,
            tierRequirement = PlayerTier.EMERGING,
            shortDescription = "High-ticket done-for-you client solutions.",
            conceptExplanation = "Formed by pairing Skill Development with Customer Acquisition. Client service generates immediate positive cashflow and direct customer feedback without upfront inventory risk.",
            practicalExample = "A specialized B2B SEO agency charging five corporate clients $4,000 monthly retainers.",
            basePrice = 290,
            quizQuestion = "What is the fastest way to validate an offer before building software?",
            quizOptionsJson = "Spending \$50,000 on software development|Delivering the solution manually as a high-ticket service first|Writing a 100-page business plan|Running banner ads to an empty landing page",
            quizCorrectIndex = 1,
            quizExplanation = "Services let you get paid to learn the customer's exact workflow before investing capital into automation."
        ),
        CardDefinition(
            id = "card_content_business",
            cardKey = "content_business",
            name = "Content Business",
            category = CardCategory.ADVANCED_OPPORTUNITY,
            rarity = CardRarity.RARE,
            tierRequirement = PlayerTier.EMERGING,
            shortDescription = "Monetized media brand powered by audience trust.",
            conceptExplanation = "Created from Content Creation + Audience Building. Generates diverse revenue streams: brand sponsorships, premium paid newsletters, events, and native merchandise.",
            practicalExample = "A specialized morning tech newsletter with 80,000 subscribers earning $15,000 per sponsored issue.",
            basePrice = 300,
            quizQuestion = "What is the primary asset of a content business?",
            quizOptionsJson = "Expensive camera gear|Trust and attention of an engaged, loyal audience|The number of daily posts published|Having complex studio lighting",
            quizCorrectIndex = 1,
            quizExplanation = "Engaged, loyal audience attention is the scarce asset that advertisers and buyers will pay to access."
        ),
        CardDefinition(
            id = "card_product_reselling",
            cardKey = "product_reselling",
            name = "Product Reselling",
            category = CardCategory.ADVANCED_OPPORTUNITY,
            rarity = CardRarity.RARE,
            tierRequirement = PlayerTier.EMERGING,
            shortDescription = "B2B and B2C physical commerce at wholesale margins.",
            conceptExplanation = "Result of combining Product Sourcing with Sales Strategy. Focuses on inventory turn speed, working capital management, and multi-channel fulfillment.",
            practicalExample = "Sourcing specialized ergonomic office accessories and retailing them across e-commerce marketplaces with a 38% net margin.",
            basePrice = 270,
            quizQuestion = "In physical product commerce, what is Working Capital Velocity?",
            quizOptionsJson = "How fast employees drive to the office|The speed at which cash invested in inventory is converted back into cash through sales|The weight of physical shipping containers|The total number of bank accounts open",
            quizCorrectIndex = 1,
            quizExplanation = "The faster you turn inventory into cash plus profit, the faster you can compound your trading capital."
        ),
        CardDefinition(
            id = "card_software_business",
            cardKey = "software_business",
            name = "Software Business",
            category = CardCategory.ADVANCED_OPPORTUNITY,
            rarity = CardRarity.EPIC,
            tierRequirement = PlayerTier.ADVANCED,
            shortDescription = "Proprietary software tool solving complex business workflows.",
            conceptExplanation = "Combining Content Business domain authority with a Digital Product creates Software as a Service (SaaS). Extremely high retention and enterprise pricing power.",
            practicalExample = "A cloud invoicing and compliance tool used by 1,200 accounting firms charging $79/seat/month.",
            basePrice = 450,
            quizQuestion = "Why do software businesses trade at the highest valuation multiples?",
            quizOptionsJson = "Because programmers are cheap to hire|High gross margins, predictable recurring revenue, and massive scalability|Software requires no updates once built|They have no competitors",
            quizCorrectIndex = 1,
            quizExplanation = "High margins, high customer switching costs, and global scalability create unmatched valuation multiples."
        ),
        CardDefinition(
            id = "card_recurring_revenue",
            cardKey = "recurring_revenue",
            name = "Recurring Revenue",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.EPIC,
            tierRequirement = PlayerTier.ADVANCED,
            shortDescription = "Contractual subscription cashflows that compound month-over-month.",
            conceptExplanation = "Subscriptions eliminate the treadmill of finding brand new customers every single month. Net Revenue Retention (NRR) above 100% allows growth even with zero new acquisitions.",
            practicalExample = "Migrating a one-time $200 license model to a $25/month subscription, tripling customer lifetime value within two years.",
            basePrice = 420,
            quizQuestion = "What is the primary risk to a subscription recurring revenue model?",
            quizOptionsJson = "High upfront revenue spikes|Customer churn (cancellations) exceeding new customer acquisitions|Having too many long-term customers|Customers paying on time every month",
            quizCorrectIndex = 1,
            quizExplanation = "If monthly churn is high, the business spends all its marketing budget merely replacing lost subscribers."
        ),
        CardDefinition(
            id = "card_systems_development",
            cardKey = "systems_development",
            name = "Systems Development",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.RARE,
            tierRequirement = PlayerTier.ADVANCED,
            shortDescription = "Standard Operating Procedures (SOPs) and delegated workflows.",
            conceptExplanation = "Documenting and systematizing operations removes the founder as the operational bottleneck. A business without systems is merely a stressful self-employed job.",
            practicalExample = "Creating step-by-step SOPs, automated checklists, and hiring trained managers to run daily client onboarding.",
            basePrice = 310,
            quizQuestion = "What is the true test of whether you own a business or just a job?",
            quizOptionsJson = "How many hours you work per week|Whether the business continues generating profit and operating smoothly if you take a 30-day absence|How fancy your corporate logo is|How many meetings you attend daily",
            quizCorrectIndex = 1,
            quizExplanation = "If your operation collapses without your continuous presence, you have a job; systems transform it into an asset."
        ),
        CardDefinition(
            id = "card_scalable_business",
            cardKey = "scalable_business",
            name = "Scalable Business",
            category = CardCategory.ADVANCED_OPPORTUNITY,
            rarity = CardRarity.LEGENDARY,
            tierRequirement = PlayerTier.STRATEGIC,
            shortDescription = "Enterprise with decoupled revenue and labor inputs.",
            conceptExplanation = "The pinnacle of wealth architecture: Combining validated Service Business with Systems Development or Software with Recurring Revenue. Revenue compounds exponentially while unit operating costs remain linear or decline.",
            practicalExample = "A multi-million dollar tech-enabled platform servicing 50,000 enterprises managed by an agile executive team.",
            basePrice = 850,
            quizQuestion = "What defines true economic scalability?",
            quizOptionsJson = "Hiring 1 worker for every 1 customer gained|Revenue grows exponentially while incremental delivery costs remain flat or decrease|Borrowing maximum debt to fund vanity marketing|Working 90 hours every week",
            quizCorrectIndex = 1,
            quizExplanation = "Scalability means revenue growth disconnects from proportional labor and capital input increases."
        ),
        CardDefinition(
            id = "card_digital_marketing",
            cardKey = "digital_product_marketing",
            name = "Digital Product Marketing",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.RARE,
            tierRequirement = PlayerTier.EMERGING,
            shortDescription = "Targeted automated funnels converting cold traffic to digital buyers.",
            conceptExplanation = "Combining Digital Product with Audience Building. Creates an evergreen automated funnel that nurtures prospects and closes transactions 24/7.",
            practicalExample = "An automated 5-day educational email sequence that generates a predictable 6.8% conversion rate on a $149 digital toolkit.",
            basePrice = 290,
            quizQuestion = "What is an 'evergreen funnel' in digital commerce?",
            quizOptionsJson = "A product made from recycled forest materials|An automated sequence that introduces, educates, and sells to new leads whenever they join|A one-time promotion that ends in 24 hours|A manual telephone sales outreach team",
            quizCorrectIndex = 1,
            quizExplanation = "Evergreen funnels run automatically on autopilot for every new subscriber regardless of when they discover your brand."
        ),
        CardDefinition(
            id = "card_automation_delegation",
            cardKey = "automation_delegation",
            name = "Automation & Delegation",
            category = CardCategory.SKILL,
            rarity = CardRarity.RARE,
            tierRequirement = PlayerTier.ADVANCED,
            shortDescription = "Software bots and specialized talent handling execution.",
            conceptExplanation = "Eliminating manual low-leverage tasks through API integrations (Zapier, custom scripts) and delegating responsibilities to high-performing specialists.",
            practicalExample = "Automating contract generation, billing, and customer provisioning, saving 25 staff hours per week.",
            basePrice = 320,
            quizQuestion = "What is the highest-leverage use of a business owner's time?",
            quizOptionsJson = "Handling customer support tickets manually all day|Strategy, capital allocation, and building scalable systems|Formatting email newsletter fonts for 4 hours|Running daily errands",
            quizCorrectIndex = 1,
            quizExplanation = "Capital allocation, high-level strategy, and systems design generate exponential leverage compared to routine operational tasks."
        ),
        CardDefinition(
            id = "card_brand_equity",
            cardKey = "brand_equity",
            name = "Brand Equity",
            category = CardCategory.STRATEGY,
            rarity = CardRarity.EPIC,
            tierRequirement = PlayerTier.STRATEGIC,
            shortDescription = "The pricing power and defensibility derived from reputation.",
            conceptExplanation = "Brand equity is the commercial value that derives from consumer perception rather than product commodity cost. It unlocks massive pricing power and customer loyalty.",
            practicalExample = "Customers paying 300% more for identical raw materials because the brand represents uncompromising quality and prestige.",
            basePrice = 500,
            quizQuestion = "What is the primary indicator of strong brand equity?",
            quizOptionsJson = "Needing to offer the lowest price in the market to survive|Pricing power: ability to charge premium prices without losing market share|Having a colourful website|Giving away free gifts with every order",
            quizCorrectIndex = 1,
            quizExplanation = "Pricing power is the clearest financial litmus test of brand equity: the ability to raise prices without suffering demand collapse."
        ),
        CardDefinition(
            id = "card_venture_capital",
            cardKey = "venture_capital",
            name = "Venture Capital",
            category = CardCategory.SPECIAL_EVENT,
            rarity = CardRarity.LEGENDARY,
            tierRequirement = PlayerTier.ELITE,
            shortDescription = "Institutional growth capital to capture winner-take-all markets.",
            conceptExplanation = "Outside risk capital traded for equity. Used to blitzscale distribution, hire elite engineering talent, and dominate market share before competitors can react.",
            practicalExample = "Raising a $5M Series A round to accelerate customer acquisition across 40 international territories.",
            basePrice = 900,
            quizQuestion = "When is raising venture capital economically justified?",
            quizOptionsJson = "Whenever you want to buy a luxury lifestyle|When a massive market opportunity requires rapid capital deployment to secure network effects and scale|Before having any idea or customer interest|To avoid working hard",
            quizCorrectIndex = 1,
            quizExplanation = "Venture capital is rocket fuel: only appropriate when the business engine is proven and massive capital will capture market leadership."
        )
    )

    val INITIAL_RECIPES = listOf(
        Recipe(
            id = "rec_digital_product",
            name = "Digital Product Forge",
            description = "Combine a validated problem solution with global instant delivery.",
            inputCardDefIdsJson = "card_product_idea,card_digital_distribution",
            outputCardDefId = "card_digital_product",
            coinCost = 50,
            isConsuming = false,
            isHidden = false,
            clue = "Product Idea + Digital Distribution"
        ),
        Recipe(
            id = "rec_content_business",
            name = "Media Monopoly",
            description = "Channel consistent valuable media into an engaged, loyal community.",
            inputCardDefIdsJson = "card_content_creation,card_audience_building",
            outputCardDefId = "card_content_business",
            coinCost = 60,
            isConsuming = false,
            isHidden = false,
            clue = "Content Creation + Audience Building"
        ),
        Recipe(
            id = "rec_product_reselling",
            name = "Wholesale Engine",
            description = "Pair cost-effective wholesale supply with disciplined sales pipelines.",
            inputCardDefIdsJson = "card_product_sourcing,card_sales_strategy",
            outputCardDefId = "card_product_reselling",
            coinCost = 70,
            isConsuming = false,
            isHidden = false,
            clue = "Product Sourcing + Sales Strategy"
        ),
        Recipe(
            id = "rec_service_business",
            name = "High-Ticket Client Practice",
            description = "Direct specialized high-income skills toward targeted customer acquisition.",
            inputCardDefIdsJson = "card_skill_development,card_customer_acquisition",
            outputCardDefId = "card_service_business",
            coinCost = 60,
            isConsuming = false,
            isHidden = false,
            clue = "Skill Development + Customer Acquisition"
        ),
        Recipe(
            id = "rec_digital_marketing",
            name = "Automated Conversion Machine",
            description = "Drive owned audience traffic to zero-marginal-cost digital products.",
            inputCardDefIdsJson = "card_digital_product,card_audience_building",
            outputCardDefId = "card_digital_marketing",
            coinCost = 80,
            isConsuming = false,
            isHidden = true,
            clue = "Digital Product + Audience Building"
        ),
        Recipe(
            id = "rec_scalable_business_ops",
            name = "Enterprise Institutional Scale",
            description = "Transform bespoke service delivery into documented Standard Operating Procedures.",
            inputCardDefIdsJson = "card_service_business,card_systems_development",
            outputCardDefId = "card_scalable_business",
            coinCost = 120,
            isConsuming = false,
            isHidden = true,
            clue = "Service Business + Systems Development"
        ),
        Recipe(
            id = "rec_software_empire",
            name = "SaaS Powerhouse",
            description = "Amplify a trusted content audience into scalable software tools.",
            inputCardDefIdsJson = "card_content_business,card_digital_product",
            outputCardDefId = "card_software_business",
            coinCost = 150,
            isConsuming = false,
            isHidden = true,
            clue = "Content Business + Digital Product"
        ),
        Recipe(
            id = "rec_compounding_scale",
            name = "Recurring Enterprise Empire",
            description = "Anchor software tooling onto compounding monthly subscription contracts.",
            inputCardDefIdsJson = "card_software_business,card_recurring_revenue",
            outputCardDefId = "card_scalable_business",
            coinCost = 200,
            isConsuming = false,
            isHidden = true,
            clue = "Software Business + Recurring Revenue"
        )
    )

    val INITIAL_MISSIONS = listOf(
        Mission(
            id = "m_tut_1",
            title = "First Steps to Capital",
            description = "Claim your 500 starting coin onboarding grant and review your collection.",
            category = MissionCategory.TUTORIAL,
            targetCount = 1,
            currentProgress = 1,
            rewardCoins = 50,
            isCompleted = true,
            isClaimed = false
        ),
        Mission(
            id = "m_tut_2",
            title = "Scholar of Capital",
            description = "Master your first wealth card by passing its comprehension quiz.",
            category = MissionCategory.TUTORIAL,
            targetCount = 1,
            currentProgress = 0,
            rewardCoins = 80,
            isCompleted = false,
            isClaimed = false
        ),
        Mission(
            id = "m_tut_3",
            title = "The Alchemist",
            description = "Combine two compatible cards in the Combination Workspace to discover a new business.",
            category = MissionCategory.TUTORIAL,
            targetCount = 1,
            currentProgress = 0,
            rewardCoins = 100,
            isCompleted = false,
            isClaimed = false
        ),
        Mission(
            id = "m_tut_4",
            title = "Marketplace Participant",
            description = "Inspect the marketplace and purchase or list a tradable card asset.",
            category = MissionCategory.TUTORIAL,
            targetCount = 1,
            currentProgress = 0,
            rewardCoins = 100,
            isCompleted = false,
            isClaimed = false
        ),
        Mission(
            id = "m_daily_1",
            title = "Daily Study Habit",
            description = "Master or review 2 financial concept cards today.",
            category = MissionCategory.DAILY,
            targetCount = 2,
            currentProgress = 0,
            rewardCoins = 60,
            isCompleted = false,
            isClaimed = false
        ),
        Mission(
            id = "m_daily_2",
            title = "Market Pulse",
            description = "Browse current marketplace listings and inspect 1 offer.",
            category = MissionCategory.DAILY,
            targetCount = 1,
            currentProgress = 0,
            rewardCoins = 40,
            isCompleted = false,
            isClaimed = false
        ),
        Mission(
            id = "m_weekly_1",
            title = "Synergy Hunter",
            description = "Perform 3 successful card combinations in the workspace.",
            category = MissionCategory.WEEKLY,
            targetCount = 3,
            currentProgress = 0,
            rewardCoins = 250,
            isCompleted = false,
            isClaimed = false
        ),
        Mission(
            id = "m_weekly_2",
            title = "Deal Maker",
            description = "Complete 2 marketplace or direct player trade transactions.",
            category = MissionCategory.WEEKLY,
            targetCount = 2,
            currentProgress = 0,
            rewardCoins = 300,
            isCompleted = false,
            isClaimed = false
        ),
        Mission(
            id = "m_milestone_1",
            title = "Emerging Tycoon",
            description = "Reach Tier 2 (Emerging) by mastering 3 concepts and discovering 1 recipe.",
            category = MissionCategory.MILESTONE,
            targetCount = 1,
            currentProgress = 0,
            rewardCoins = 400,
            isCompleted = false,
            isClaimed = false
        )
    )

    val INITIAL_ACHIEVEMENTS = listOf(
        Achievement(
            id = "ach_first_steps",
            title = "Foundational Capital",
            description = "Received starting balance of 500 virtual coins.",
            rewardCoins = 50,
            isUnlocked = true,
            unlockedAt = System.currentTimeMillis()
        ),
        Achievement(
            id = "ach_first_mastery",
            title = "Principle Mastered",
            description = "Score 100% on any card's knowledge quiz.",
            rewardCoins = 75,
            isUnlocked = false,
            unlockedAt = null
        ),
        Achievement(
            id = "ach_first_combo",
            title = "Idea Synthesis",
            description = "Discover your very first card combination recipe.",
            rewardCoins = 100,
            isUnlocked = false,
            unlockedAt = null
        ),
        Achievement(
            id = "ach_market_mogul",
            title = "Market Participant",
            description = "Complete your first marketplace trade or purchase.",
            rewardCoins = 100,
            isUnlocked = false,
            unlockedAt = null
        ),
        Achievement(
            id = "ach_five_masteries",
            title = "Business Polymath",
            description = "Master 5 distinct wealth principle concepts.",
            rewardCoins = 250,
            isUnlocked = false,
            unlockedAt = null
        ),
        Achievement(
            id = "ach_rare_discovery",
            title = "Enterprise Architect",
            description = "Craft a Rare or higher business opportunity card.",
            rewardCoins = 300,
            isUnlocked = false,
            unlockedAt = null
        ),
        Achievement(
            id = "ach_tier_emerging",
            title = "Emerging Status",
            description = "Advance your player tier to Emerging.",
            rewardCoins = 200,
            isUnlocked = false,
            unlockedAt = null
        ),
        Achievement(
            id = "ach_tier_advanced",
            title = "Strategic Operator",
            description = "Advance your player tier to Advanced.",
            rewardCoins = 500,
            isUnlocked = false,
            unlockedAt = null
        )
    )

    // Simulated community peers providing active trading and market depth
    val INITIAL_MARKET_LISTINGS = listOf(
        MarketListing(
            id = "list_peer_1",
            sellerUserId = "peer_marcus",
            sellerDisplayName = "Marcus Vance (Dealmaker)",
            cardDefId = "card_digital_distribution",
            inventoryInstanceId = null,
            price = 95,
            listedAt = System.currentTimeMillis() - 3600000,
            status = ListingStatus.ACTIVE,
            isAiTrader = true
        ),
        MarketListing(
            id = "list_peer_2",
            sellerUserId = "peer_elena",
            sellerDisplayName = "Elena Rostova (Growth Lead)",
            cardDefId = "card_audience_building",
            inventoryInstanceId = null,
            price = 140,
            listedAt = System.currentTimeMillis() - 7200000,
            status = ListingStatus.ACTIVE,
            isAiTrader = true
        ),
        MarketListing(
            id = "list_peer_3",
            sellerUserId = "peer_devon",
            sellerDisplayName = "Devon Chen (Operations)",
            cardDefId = "card_product_sourcing",
            inventoryInstanceId = null,
            price = 125,
            listedAt = System.currentTimeMillis() - 10800000,
            status = ListingStatus.ACTIVE,
            isAiTrader = true
        ),
        MarketListing(
            id = "list_peer_4",
            sellerUserId = "peer_zahra",
            sellerDisplayName = "Zahra Al-Mansoor (Strategist)",
            cardDefId = "card_market_research",
            inventoryInstanceId = null,
            price = 90,
            listedAt = System.currentTimeMillis() - 14400000,
            status = ListingStatus.ACTIVE,
            isAiTrader = true
        ),
        MarketListing(
            id = "list_peer_5",
            sellerUserId = "peer_kai",
            sellerDisplayName = "Kai Sterling (Angel Investor)",
            cardDefId = "card_customer_acquisition",
            inventoryInstanceId = null,
            price = 155,
            listedAt = System.currentTimeMillis() - 18000000,
            status = ListingStatus.ACTIVE,
            isAiTrader = true
        )
    )

    val INITIAL_TRADE_OFFERS = listOf(
        TradeOffer(
            id = "trade_offer_marcus",
            senderUserId = "peer_marcus",
            senderDisplayName = "Marcus Vance",
            receiverUserId = "current_user",
            receiverDisplayName = "You",
            offeredCardDefIdsJson = "card_digital_distribution",
            offeredCoins = 30,
            requestedCardDefIdsJson = "card_saver",
            requestedCoins = 0,
            status = TradeStatus.PENDING,
            createdAt = System.currentTimeMillis() - 1800000,
            note = "I need 'The Saver' for my liquidity pool. I'm offering 'Digital Distribution' plus 30 coins bonus!"
        ),
        TradeOffer(
            id = "trade_offer_elena",
            senderUserId = "peer_elena",
            senderDisplayName = "Elena Rostova",
            receiverUserId = "current_user",
            receiverDisplayName = "You",
            offeredCardDefIdsJson = "card_sales_strategy",
            offeredCoins = 0,
            requestedCardDefIdsJson = "card_content_creation",
            requestedCoins = 20,
            status = TradeStatus.PENDING,
            createdAt = System.currentTimeMillis() - 3600000,
            note = "Looking to swap my Sales Strategy for your Content Creation to complete my media pipeline."
        )
    )
}
