package com.aispeech.nativedemo.bean;

import java.util.List;

/**
 * Created by nemo on 2019-05-30.
 */
public class WeatherBean {

    /**
     * widgetName : weather
     * duiWidget : custom
     * cityName : 苏州市
     * errCode : 0
     * forecastChoose : [{"conditionDayNight":"阴","conditionDay":"阴","windLevelDay":"2","temptip":"冷热适宜，感觉挺舒适","tempDay":"25","windDirDay":"东北风","updatetime":"2019-05-30 11:00:00","conditionNight":"阴","predictDate":"2019-05-30","tempNight":"20"}]
     * aqi : {"no2":"39.0","cityName":"苏州市","coC":"4","so2C":"9.0","rank":"309/578","tip":"享受新鲜空气吧","pm10C":"43.0","AQL":"优","value":"43"}
     * name : weather
     * forecast : [{"conditionDayNight":"阴","conditionDay":"阴","windLevelDay":"2","temptip":"冷热适宜，感觉挺舒适","tempDay":"25","windDirDay":"东北风","updatetime":"2019-05-30 11:00:00","conditionNight":"阴","predictDate":"2019-05-30","tempNight":"20"},{"conditionDayNight":"阴转多云","conditionDay":"阴","windLevelDay":"3","temptip":"有点热，记得多喝水","tempDay":"27","windDirDay":"东南风转东风","updatetime":"2019-05-30 11:00:00","conditionNight":"多云","predictDate":"2019-05-31","tempNight":"19"},{"conditionDayNight":"多云","conditionDay":"多云","windLevelDay":"3","temptip":"冷热适宜，感觉挺舒适","tempDay":"26","windDirDay":"东风","updatetime":"2019-05-30 11:00:00","conditionNight":"多云","predictDate":"2019-06-01","tempNight":"21"},{"conditionDayNight":"多云转阴","conditionDay":"多云","windLevelDay":"3","temptip":"有点热，记得多喝水","tempDay":"27","windDirDay":"东南风","updatetime":"2019-05-30 11:00:00","conditionNight":"阴","predictDate":"2019-06-02","tempNight":"19"},{"conditionDayNight":"多云转晴","conditionDay":"多云","windLevelDay":"3","temptip":"天气炎热，外出要注意防晒","tempDay":"31","windDirDay":"东南风","updatetime":"2019-05-30 11:00:00","conditionNight":"晴","predictDate":"2019-06-03","tempNight":"20"},{"conditionDayNight":"晴","conditionDay":"晴","windLevelDay":"3","temptip":"天气炎热，外出要注意防晒","tempDay":"33","windDirDay":"东南风","updatetime":"2019-05-30 11:00:00","conditionNight":"晴","predictDate":"2019-06-04","tempNight":"21"}]
     * type : custom
     * baseHit : [{"rain":{"hit":"false","tip":"祝你出门好心情"},"frost":{"hit":"false","tip":"祝你出门好心情"},"snow":{"hit":"false","tip":"祝你出门好心情"},"hailstone":{"hit":"false"},"thunder":{"hit":"false"},"sandStorm":{"hit":"false"},"flash":{"hit":"false"},"fog":{"hit":"false"}}]
     */

    private String widgetName;
    private String duiWidget;
    private String cityName;
    private int errCode;
    private AqiBean aqi;
    private String name;
    private String type;
    private List<ForecastChooseBean> forecastChoose;
    private List<ForecastBean> forecast;
    private List<BaseHitBean> baseHit;

    public String getWidgetName() {
        return widgetName;
    }

    public void setWidgetName(String widgetName) {
        this.widgetName = widgetName;
    }

    public String getDuiWidget() {
        return duiWidget;
    }

    public void setDuiWidget(String duiWidget) {
        this.duiWidget = duiWidget;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public AqiBean getAqi() {
        return aqi;
    }

    public void setAqi(AqiBean aqi) {
        this.aqi = aqi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ForecastChooseBean> getForecastChoose() {
        return forecastChoose;
    }

    public void setForecastChoose(List<ForecastChooseBean> forecastChoose) {
        this.forecastChoose = forecastChoose;
    }

    public List<ForecastBean> getForecast() {
        return forecast;
    }

    public void setForecast(List<ForecastBean> forecast) {
        this.forecast = forecast;
    }

    public List<BaseHitBean> getBaseHit() {
        return baseHit;
    }

    public void setBaseHit(List<BaseHitBean> baseHit) {
        this.baseHit = baseHit;
    }

    public static class AqiBean {
        /**
         * no2 : 39.0
         * cityName : 苏州市
         * coC : 4
         * so2C : 9.0
         * rank : 309/578
         * tip : 享受新鲜空气吧
         * pm10C : 43.0
         * AQL : 优
         * value : 43
         */

        private String no2;
        private String cityName;
        private String coC;
        private String so2C;
        private String rank;
        private String tip;
        private String pm10C;
        private String AQL;
        private String value;

        public String getNo2() {
            return no2;
        }

        public void setNo2(String no2) {
            this.no2 = no2;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getCoC() {
            return coC;
        }

        public void setCoC(String coC) {
            this.coC = coC;
        }

        public String getSo2C() {
            return so2C;
        }

        public void setSo2C(String so2C) {
            this.so2C = so2C;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getTip() {
            return tip;
        }

        public void setTip(String tip) {
            this.tip = tip;
        }

        public String getPm10C() {
            return pm10C;
        }

        public void setPm10C(String pm10C) {
            this.pm10C = pm10C;
        }

        public String getAQL() {
            return AQL;
        }

        public void setAQL(String AQL) {
            this.AQL = AQL;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class ForecastChooseBean {
        /**
         * conditionDayNight : 阴
         * conditionDay : 阴
         * windLevelDay : 2
         * temptip : 冷热适宜，感觉挺舒适
         * tempDay : 25
         * windDirDay : 东北风
         * updatetime : 2019-05-30 11:00:00
         * conditionNight : 阴
         * predictDate : 2019-05-30
         * tempNight : 20
         */

        private String conditionDayNight;
        private String conditionDay;
        private String windLevelDay;
        private String temptip;
        private String tempDay;
        private String windDirDay;
        private String updatetime;
        private String conditionNight;
        private String predictDate;
        private String tempNight;

        public String getConditionDayNight() {
            return conditionDayNight;
        }

        public void setConditionDayNight(String conditionDayNight) {
            this.conditionDayNight = conditionDayNight;
        }

        public String getConditionDay() {
            return conditionDay;
        }

        public void setConditionDay(String conditionDay) {
            this.conditionDay = conditionDay;
        }

        public String getWindLevelDay() {
            return windLevelDay;
        }

        public void setWindLevelDay(String windLevelDay) {
            this.windLevelDay = windLevelDay;
        }

        public String getTemptip() {
            return temptip;
        }

        public void setTemptip(String temptip) {
            this.temptip = temptip;
        }

        public String getTempDay() {
            return tempDay;
        }

        public void setTempDay(String tempDay) {
            this.tempDay = tempDay;
        }

        public String getWindDirDay() {
            return windDirDay;
        }

        public void setWindDirDay(String windDirDay) {
            this.windDirDay = windDirDay;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getConditionNight() {
            return conditionNight;
        }

        public void setConditionNight(String conditionNight) {
            this.conditionNight = conditionNight;
        }

        public String getPredictDate() {
            return predictDate;
        }

        public void setPredictDate(String predictDate) {
            this.predictDate = predictDate;
        }

        public String getTempNight() {
            return tempNight;
        }

        public void setTempNight(String tempNight) {
            this.tempNight = tempNight;
        }
    }

    public static class ForecastBean {
        /**
         * conditionDayNight : 阴
         * conditionDay : 阴
         * windLevelDay : 2
         * temptip : 冷热适宜，感觉挺舒适
         * tempDay : 25
         * windDirDay : 东北风
         * updatetime : 2019-05-30 11:00:00
         * conditionNight : 阴
         * predictDate : 2019-05-30
         * tempNight : 20
         */

        private String conditionDayNight;
        private String conditionDay;
        private String windLevelDay;
        private String temptip;
        private String tempDay;
        private String windDirDay;
        private String updatetime;
        private String conditionNight;
        private String predictDate;
        private String tempNight;

        public String getConditionDayNight() {
            return conditionDayNight;
        }

        public void setConditionDayNight(String conditionDayNight) {
            this.conditionDayNight = conditionDayNight;
        }

        public String getConditionDay() {
            return conditionDay;
        }

        public void setConditionDay(String conditionDay) {
            this.conditionDay = conditionDay;
        }

        public String getWindLevelDay() {
            return windLevelDay;
        }

        public void setWindLevelDay(String windLevelDay) {
            this.windLevelDay = windLevelDay;
        }

        public String getTemptip() {
            return temptip;
        }

        public void setTemptip(String temptip) {
            this.temptip = temptip;
        }

        public String getTempDay() {
            return tempDay;
        }

        public void setTempDay(String tempDay) {
            this.tempDay = tempDay;
        }

        public String getWindDirDay() {
            return windDirDay;
        }

        public void setWindDirDay(String windDirDay) {
            this.windDirDay = windDirDay;
        }

        public String getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(String updatetime) {
            this.updatetime = updatetime;
        }

        public String getConditionNight() {
            return conditionNight;
        }

        public void setConditionNight(String conditionNight) {
            this.conditionNight = conditionNight;
        }

        public String getPredictDate() {
            return predictDate;
        }

        public void setPredictDate(String predictDate) {
            this.predictDate = predictDate;
        }

        public String getTempNight() {
            return tempNight;
        }

        public void setTempNight(String tempNight) {
            this.tempNight = tempNight;
        }
    }

    public static class BaseHitBean {
        /**
         * rain : {"hit":"false","tip":"祝你出门好心情"}
         * frost : {"hit":"false","tip":"祝你出门好心情"}
         * snow : {"hit":"false","tip":"祝你出门好心情"}
         * hailstone : {"hit":"false"}
         * thunder : {"hit":"false"}
         * sandStorm : {"hit":"false"}
         * flash : {"hit":"false"}
         * fog : {"hit":"false"}
         */

        private RainBean rain;
        private FrostBean frost;
        private SnowBean snow;
        private HailstoneBean hailstone;
        private ThunderBean thunder;
        private SandStormBean sandStorm;
        private FlashBean flash;
        private FogBean fog;

        public RainBean getRain() {
            return rain;
        }

        public void setRain(RainBean rain) {
            this.rain = rain;
        }

        public FrostBean getFrost() {
            return frost;
        }

        public void setFrost(FrostBean frost) {
            this.frost = frost;
        }

        public SnowBean getSnow() {
            return snow;
        }

        public void setSnow(SnowBean snow) {
            this.snow = snow;
        }

        public HailstoneBean getHailstone() {
            return hailstone;
        }

        public void setHailstone(HailstoneBean hailstone) {
            this.hailstone = hailstone;
        }

        public ThunderBean getThunder() {
            return thunder;
        }

        public void setThunder(ThunderBean thunder) {
            this.thunder = thunder;
        }

        public SandStormBean getSandStorm() {
            return sandStorm;
        }

        public void setSandStorm(SandStormBean sandStorm) {
            this.sandStorm = sandStorm;
        }

        public FlashBean getFlash() {
            return flash;
        }

        public void setFlash(FlashBean flash) {
            this.flash = flash;
        }

        public FogBean getFog() {
            return fog;
        }

        public void setFog(FogBean fog) {
            this.fog = fog;
        }

        public static class RainBean {
            /**
             * hit : false
             * tip : 祝你出门好心情
             */

            private String hit;
            private String tip;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }
        }

        public static class FrostBean {
            /**
             * hit : false
             * tip : 祝你出门好心情
             */

            private String hit;
            private String tip;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }
        }

        public static class SnowBean {
            /**
             * hit : false
             * tip : 祝你出门好心情
             */

            private String hit;
            private String tip;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }

            public String getTip() {
                return tip;
            }

            public void setTip(String tip) {
                this.tip = tip;
            }
        }

        public static class HailstoneBean {
            /**
             * hit : false
             */

            private String hit;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }
        }

        public static class ThunderBean {
            /**
             * hit : false
             */

            private String hit;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }
        }

        public static class SandStormBean {
            /**
             * hit : false
             */

            private String hit;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }
        }

        public static class FlashBean {
            /**
             * hit : false
             */

            private String hit;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }
        }

        public static class FogBean {
            /**
             * hit : false
             */

            private String hit;

            public String getHit() {
                return hit;
            }

            public void setHit(String hit) {
                this.hit = hit;
            }
        }
    }
}
