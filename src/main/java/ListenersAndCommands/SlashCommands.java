package ListenersAndCommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.*;

public class SlashCommands {
    private final JDA bot;
    private final OptionData category = new OptionData(OptionType.STRING ,"category", "This is the name of " +
            "the category.",true);
    public SlashCommandData bulkChannelCreation = Commands.slash("bulkcreate", "Bulk creates channels, default is 1.")
            .addOption(OptionType.STRING ,"category", "This is the name of " +
                    "the category.",true);
    protected int bulkChannelAmount =1;

    public SlashCommands (JDA bot){
        this.bot = bot;
        bulkChannelCreateor(1);
        channelCreation();
    }
    public SlashCommands (JDA bot, int bulkChannelAmount){
        this.bot = bot;
        bulkChannelCreateor(bulkChannelAmount); //creates channels for the given amount, default is 1
        channelCreation();
    }
    public void channelCreation(){
        CommandData bulk = Commands.slash("bulk","Prerequisite for bulk channel creation " +
                        "if the number is >1")
                .addOption(OptionType.INTEGER, "amount", "Amount of channels " +
                        "you aim to create using /bulkcreate", true);

        OptionData[] navigationOptions = new OptionData[]{
                new OptionData(OptionType.STRING, "navcat", "Name of the category in which" +
                        " the navigation channel exists/ will be created", true),
                new OptionData(OptionType.STRING, "navchn", "Name of the channel in " +
                        "which the navigation embed will be sent.", true)
        };

        CommandData navigation =//send navigation embed
                        Commands.slash("navigation", "Sends a navigation message containing the channel" +
                                "links in a selected channel.")
                                .addSubcommands(new SubcommandData("cattonav", "Sends an" +
                                        "embed containing all channels and their links in this category")
                                        .addOptions(navigationOptions)
                                        .addOption(OptionType.CHANNEL, "copycategory", "An embed " +
                                                "containing all of the channel links of this category will be created."
                                        ,true))
                                .addSubcommands(new SubcommandData("chantonav", "Creates an embed conta" +
                                        "ining the link of this channel.")
                                        .addOptions(navigationOptions)
                                        .addOption(OptionType.CHANNEL, "channel", "Name of the " +
                                                "Channel.", true));
        bot.updateCommands().addCommands(bulk, bulkChannelCreation, navigation).queue();
    }

    private void bulkChannelCreateor(int amountOfChannels){
        if(amountOfChannels<2){
            return;
        }

        for(int i =0; i< amountOfChannels; i++){
            bulkChannelCreation
                    .addOption(OptionType.STRING, "channel"+i+"", "Name of channel"+i+".", true);
            bot.upsertCommand(bulkChannelCreation).queue();
        }
        bulkChannelAmount = amountOfChannels;
    }


    public int getBulkChannelAmount() {
        return bulkChannelAmount;
    }
}
